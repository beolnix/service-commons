package com.lngbk.commons.management

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

/**
  * Created by beolnix on 09/09/16.
  */
object SystemManager {

  private val _config = ConfigFactory.load("akka-application")
  private val _system = ActorSystem("LngbkSystem", _config)

  def system = _system

}
