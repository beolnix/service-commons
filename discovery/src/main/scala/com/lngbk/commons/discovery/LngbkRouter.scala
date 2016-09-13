package com.lngbk.commons.discovery

import java.util
import java.util.{Timer, TimerTask}

import akka.actor.{Actor, ActorPath, ActorRef, ActorSystem, Address, AddressFromURIString, Props, RootActorPath}
import akka.routing.{RoundRobinGroup, RoundRobinPool, Router}
import com.lngbk.commons.discovery.consul.ConsulClient
import akka.pattern.ask
import akka.util.Timeout
import org.slf4j.LoggerFactory

import scala.concurrent.duration
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by beolnix on 28/08/16.
  */
class LngbkRouter(val serviceName: String, val system: ActorSystem, val poolSize: Int = 5, actorPath: Option[ActorPath] = None) {

  private val logger = LoggerFactory.getLogger(LngbkRouter.getClass)

  import com.lngbk.commons.discovery.utils.DiscoveryUtils._

  // constants
  private val SERVICES_REFRESH_PERIOD = 1000L

  // state
  @volatile private var _services = Set[Address]()
  @volatile private var _remote = {
    if (_services.isEmpty && actorPath.isEmpty)
      Option.empty
    else if (actorPath.nonEmpty) {
      val pathes = scala.collection.immutable.Iterable(actorPath.get.address.toString + "/user/" + serviceName)
      Option(system.actorOf(RoundRobinGroup(pathes).props(), serviceName))
    } else {
      val pathes = _services.map(address => address.toString + "/user/" + serviceName)
      Option(system.actorOf(RoundRobinGroup(pathes).props(), serviceName))
    }
  }

  // state maintenance
  private val update: () => Unit = () => {
    val newServices = ConsulClient.getServiceAddresses(serviceName)

    if (_services != newServices && newServices.nonEmpty) {
      logger.info(s"Got routes update for $serviceName service. Diff: ${_services.diff(newServices)}")
      val pathes = _services.map(address => address.toString + "/authentication")
      _remote = Option(system.actorOf(RoundRobinGroup(pathes).props(), serviceName))
      _services = newServices
    } else if (_services.isEmpty && newServices.isEmpty) {
      logger.info(s"No routes registered for the $serviceName. Next check in $SERVICES_REFRESH_PERIOD ms")
    }

  }

  private val periodicalServicesUpdater = new Timer()
  periodicalServicesUpdater.schedule(
    update,
    0L,
    SERVICES_REFRESH_PERIOD
  )

  // accessors and other interactions
  def remote = _remote

  implicit val timeout = Timeout(15 seconds)

  def ?(that: Object)(implicit timeout: Timeout = Duration(40, duration.SECONDS), sender: ActorRef = Actor.noSender) = _remote match {
    case Some(value) => {
      value.ask(that)
    }
    case None => throw new RuntimeException(s"router not defined for $serviceName")
  }

  def !(that: Object) = _remote match {
    case Some(value) => value ! that
    case None => throw new RuntimeException(s"router not defined for $serviceName")
  }

  def isReady: Boolean = _remote.nonEmpty

}

object LngbkRouter {
  def apply(serviceName: String, system: ActorSystem, poolSize: Int = 5, actorPath: Option[ActorPath] = None) = new LngbkRouter(serviceName, system, poolSize, actorPath)

}
