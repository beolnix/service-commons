package com.lngbk.commons.discovery

import java.util.Timer

import akka.actor.{Actor, ActorPath, ActorRef, ActorSystem, Address}
import akka.routing.RoundRobinGroup
import com.lngbk.commons.discovery.consul.ConsulClient
import akka.pattern.ask
import akka.util.Timeout
import org.slf4j.LoggerFactory

import scala.concurrent.duration
import scala.concurrent.duration._
import scala.language.postfixOps

object LngbkRouter {
  def apply(serviceName: String, system: ActorSystem, poolSize: Int = 5, actorPath: Option[ActorPath] = None) = new LngbkRouter(serviceName, system, poolSize, actorPath)

}

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
      logger.info(s"Got routes update for $serviceName service. Diff: ${newServices.diff(_services)}")
      val pathes = newServices.map(address => address.toString + "/user/" + serviceName)
      _remote = Option(system.actorOf(RoundRobinGroup(pathes).props(), serviceName))
      _services = newServices
    } else if (_services.isEmpty && newServices.isEmpty) {
      logger.info(s"No routes registered for the $serviceName. Next check in $SERVICES_REFRESH_PERIOD ms")
    }

  }

  private val periodicalServicesUpdater = new Timer
  if (actorPath.isEmpty) {
    logger.info(s"Actor path for $serviceName hasn't been passed, " +
      s"start periodical check in service registry for updates.")
    periodicalServicesUpdater.schedule(
      update,
      0L,
      SERVICES_REFRESH_PERIOD
    )
  } else {
    logger.info(s"Actor path has been provided as $actorPath. Skip service registry periodical check initialization.")
  }

  // accessors and other interactions
  def remote = _remote

  implicit val timeout = Timeout(15 seconds)

  def ?(that: Object)(implicit timeout: Timeout = Duration(40, duration.SECONDS), sender: ActorRef = Actor.noSender) = _remote match {
    case Some(value) => value ? that
    case None => throw new RuntimeException(s"router not defined for $serviceName")
  }

  def !(that: Object) = _remote match {
    case Some(value) => value ! that
    case None => throw new RuntimeException(s"router not defined for $serviceName")
  }

  def isReady: Boolean = _remote.nonEmpty

}
