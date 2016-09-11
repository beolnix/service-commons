package com.lngbk.commons.api.server

import akka.actor.Actor
import com.lngbk.commons.api.dto.{LngbkVersionRequest, LngbkVersionResponse, VersionDTO}
import com.lngbk.commons.api.util.VersionHelper
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by beolnix on 10/09/16.
  */
abstract class LngbkActor extends Actor {
  private val logger: Logger = LoggerFactory.getLogger(classOf[LngbkActor])

  override def receive: Receive = {
    case LngbkVersionRequest(requestUuid) => {
      val response = LngbkVersionResponse(
        VersionHelper.minCompatibleVersion, VersionHelper.currentVersion, Option.empty
      )
      logger.info(s"Got version request, send back: $response")
      sender() ! response
    }
    case msg => process(msg)
  }

  def process: Receive
}
