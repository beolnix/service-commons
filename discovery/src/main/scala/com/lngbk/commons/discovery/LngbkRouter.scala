package com.lngbk.commons.discovery

import java.util.{Timer, TimerTask}

import akka.actor.{ActorSystem, Address, AddressFromURIString, Props}
import akka.remote.routing.RemoteRouterConfig
import akka.routing.{RoundRobinPool, Router}
import com.lngbk.commons.discovery.consul.ConsulClient

/**
  * Created by beolnix on 28/08/16.
  */
class LngbkRouter(val serviceName: String, val system: ActorSystem) {
  import com.lngbk.commons.discovery.utils.DiscoveryUtils._

  // constants
  private val SERVICES_REFRESH_PERIOD = 1000L
  private val ROUTER_POOL_SIZE = 50

  // state
  @volatile private var _services = ConsulClient.getServiceAddresses(serviceName)
  @volatile private var _remote = system.actorOf(
    RemoteRouterConfig(RoundRobinPool(ROUTER_POOL_SIZE), _services).props(
      Props()))

  // state maintenance
  private val update: () => Unit = () => {
    val newServices = ConsulClient.getServiceAddresses(serviceName)

    if (_services != newServices) {
      _remote = system.actorOf(
        RemoteRouterConfig(RoundRobinPool(ROUTER_POOL_SIZE), newServices).props(
          Props()))
      _services = newServices
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

  def !(that: Object): Unit = {
    remote ! that
  }

}

object LngbkRouter {
  def apply(serviceName: String, system: ActorSystem) = new LngbkRouter(serviceName, system)

}
