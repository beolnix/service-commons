package com.lngbk.commons.discovery

import java.net.InetAddress

import com.google.common.net.HostAndPort
import com.orbitz.consul.{AgentClient, Consul}
import com.typesafe.config.ConfigFactory



/**
  * Created by beolnix on 28/08/16.
  */
object ConsulClient {
  private val config = ConfigFactory.load()
  private val CONSUL_IP = config.getString("consul.agent.ip")
  private val CONSUL_PORT = config.getInt("consul.agent.port")

  private val consul: Consul = Consul.builder()
      .withHostAndPort(HostAndPort.fromParts(CONSUL_IP, CONSUL_PORT))
      .build()
  val lngbkConsul: AgentClient = consul.agentClient()
  val healthClient = consul.healthClient()

}
