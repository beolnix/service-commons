package com.lngbk.commons.management

import akka.actor.{Actor, Props}
import akka.util.Timeout

object HealthCheckActor {
  def props(implicit timeout: Timeout) = Props(new HealthCheckActor)
  case class Ping()
  case class Pong()
  case class Error()
}

class HealthCheckActor extends Actor {
  import com.lngbk.commons.management.HealthCheckActor._
  override def receive: Receive = {
    case Ping => {
      sender() ! Pong
    }
  }
}
