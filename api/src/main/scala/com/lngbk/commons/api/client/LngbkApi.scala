package com.lngbk.commons.api.client

import akka.actor.{ActorPath, Props, RootActorPath}
import akka.remote.ContainerFormats.ActorRef
import com.lngbk.commons.api.dto.{LngbkVersionRequest, LngbkVersionResponse}
import com.lngbk.commons.api.errors.{ApiCriticalError, CommonErrorCodes}
import com.lngbk.commons.api.util.VersionHelper
import com.lngbk.commons.config.ServiceIdentity
import com.lngbk.commons.discovery.LngbkRouter
import com.lngbk.commons.management.SystemManager
import com.lngbk.commons.management.bootstrap.DependenciesManager
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Success, Try}

/**
  * Created by beolnix on 10/09/16.
  */
abstract class LngbkApi(val serviceName: String, poolSize: Int = 5, actorPath: Option[ActorPath] = None) {

  private val logger: Logger = LoggerFactory.getLogger(classOf[LngbkApi])

  private var _router: Option[LngbkRouter] = None

  def initApi(): Unit = {
    if (ServiceIdentity.readFromConfig.serviceType != serviceName) {
      logger.info(s"Initialize $serviceName API")
      initRouter()
      checkVersion()
      DependenciesManager.checkIn(router)
    } else {
      logger.info(s"Skip $serviceName API initialization because it is only needed on a client side.")
    }
  }

  private def initRouter(): Unit = {
    _router = Option(
      LngbkRouter(
        serviceName,
        SystemManager.system,
        poolSize,
        actorPath
      )
    )
  }

  def router: LngbkRouter = _router match {
    case Some(value) => value
    case None => throw new RuntimeException(s"LngbkApi $serviceName initializatino error")
  }

  private def checkVersion(): Unit = {
    val apiVersionDTO = VersionHelper.apiVersion(serviceName)
    val response = router ? LngbkVersionRequest()
    logger.info("Waiting for version response")
    val result: Option[Try[Any]] = Await.ready(response, Duration.Inf).value
    logger.info(s"Got version response: $result")

    result match {
      case Some(Success(LngbkVersionResponse(minCompatible, current))) => {
        if (apiVersionDTO.compareTo(minCompatible) == -1) {
          logger.error(s"Version of the API incompatible with version service. Api version: $apiVersionDTO; service version: $minCompatible")
          throw new ApiCriticalError(CommonErrorCodes.API_INCOMPATIBLE_VERSION)
        } else if (apiVersionDTO.compareTo(minCompatible) >= 0 && apiVersionDTO.compareTo(current) < 0) {
          logger.info(s"Version of the API compatible with service version but less. Api version: $apiVersionDTO; service version: $current")
        } else {
          logger.info(s"Version of the API is inline with service version. Api version: $apiVersionDTO; service version: $current")
        }
      }

      case _ => throw new ApiCriticalError(CommonErrorCodes.PIZDEC)
    }
  }
}
