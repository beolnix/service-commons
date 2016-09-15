package com.lngbk.commons.service

import akka.actor.{Actor, Props}
import com.lngbk.commons.management.SystemManager
import com.lngbk.commons.management.bootstrap.ServiceBootstrapDirector
import org.slf4j.{Logger, LoggerFactory}
import scala.reflect.ClassTag

/**
  * Created by beolnix on 10/09/16.
  */
abstract class LngbkService[T <: Actor](val waitForDependencies: Boolean,
                                        val register: Boolean,
                                        val serviceName: String)(implicit actor: scala.reflect.ClassTag[T]) {
  private val logger: Logger = LoggerFactory.getLogger(serviceName)

  withDependencies()
  ServiceBootstrapDirector.initService(waitForDependencies, register)
  val actorRef = SystemManager.system.actorOf(Props[T], serviceName)
  logger.info(s"Service $serviceName started successfully")

  def withDependencies(): Unit
}