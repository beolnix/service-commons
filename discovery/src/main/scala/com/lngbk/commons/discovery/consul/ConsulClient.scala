package com.lngbk.commons.discovery.consul

import akka.actor.Address
import com.google.common.net.HostAndPort
import com.lngbk.commons.config.ServiceIdentity
import com.lngbk.commons.discovery.dto.LngbkAddress
import com.orbitz.consul.model.health.ServiceHealth
import com.orbitz.consul.{AgentClient, Consul}
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

import collection.JavaConversions._
import scala.collection.immutable.HashSet



/**
  * Created by beolnix on 28/08/16.
  */
object ConsulClient {

  // constants
  private val SERVICE_REGISTRATION_TTL = 5L // service registration TTL in seconds
  private val logger = LoggerFactory.getLogger(ConsulClient.getClass)

  // dependencies
  private val serviceIdentity = ServiceIdentity.readFromConfig
  private val config = ConfigFactory.load()
  private val CONSUL_IP = config.getString("consul.agent.ip")
  private val CONSUL_PORT = config.getInt("consul.agent.port")
  private val INTERFACE = config.getString("service.interface.ip")

  private val consul: Consul = Consul.builder()
      .withHostAndPort(HostAndPort.fromParts(CONSUL_IP, CONSUL_PORT))
      .build()
  private val lngbkConsul: AgentClient = consul.agentClient()
  private val healthClient = consul.healthClient()

  def register() = {
    lngbkConsul.register(
      serviceIdentity.serviceAkkaPort,
      HostAndPort.fromParts(INTERFACE, serviceIdentity.serviceAkkaPort),
      SERVICE_REGISTRATION_TTL,
      serviceIdentity.serviceType.toString,
      serviceIdentity.serviceId); // registers with a TTL of 3 seconds

    lngbkConsul.pass(serviceIdentity.serviceId)

    RegistrationCheck.startPeriodicalRegistrationUpdate()
  }

  def getServiceAddresses(serviceName: String): Set[Address] =
    healthClient.getHealthyServiceInstances(serviceName).getResponse
      .map(serviceHealth =>
        Address(
          "akka.tcp",
          "LngbkSystem",
          serviceHealth.getNode.getAddress,
          serviceHealth.getService.getPort
        )
      ).to[HashSet[Address]]

}
