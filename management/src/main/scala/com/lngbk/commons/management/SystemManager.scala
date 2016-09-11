package com.lngbk.commons.management

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

/**
  * Created by beolnix on 09/09/16.
  */
object SystemManager {

  private val logger = LoggerFactory.getLogger(SystemManager.getClass)

  private val _config = ConfigFactory.load("akka-application")
  private var _system = Option.empty[ActorSystem]

  def initWithSystem(embeddedSystem: ActorSystem): Unit = {
    logger.info(s"Init system with $embeddedSystem")
    _system = Some(embeddedSystem)
  }

  def system = {
    if (_system.nonEmpty) {
      _system.get
    } else {
      logger.info(s"Init default system")
      _system = Some(ActorSystem("LngbkSystem", _config))
      _system.get
    }
  }

}
