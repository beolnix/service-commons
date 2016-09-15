package com.lngbk.commons.discovery

import akka.actor.{Actor, Props}
import akka.util.Timeout
import com.lngbk.commons.discovery.HealthCheckActor.{Ping, Pong}

object HealthCheckActor {
  def props(implicit timeout: Timeout) = Props(new HealthCheckActor)
  case class Ping()
  case class Pong()
  case class Error()
}

class HealthCheckActor extends Actor {
  override def receive: Receive = {
    case Ping => {
      println(s"Got a ping from $sender")
      sender() ! Pong
    }
  }
}
