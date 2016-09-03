package com.lngbk.commons.consul


import java.net.{Inet4Address, InetAddress}

import com.ning.http.client.AsyncHttpClientConfig

import scala.concurrent.ExecutionContext.Implicits.global
import com.typesafe.config.ConfigFactory
import consul.Consul
import play.api.libs.ws.{WS, WSClient}
import play.api.libs.ws.ning.{NingAsyncHttpClientConfigBuilder, NingWSClient}
import play.api.libs.ws.ning._
import play.api.libs.ws._

/**
  * Created by beolnix on 28/08/16.
  */
object ConsulClient {
  private val config = ConfigFactory.load()
  private val CONSUL_IP = InetAddress.getByName(config.getString("consul.agent.ip"))
  private val CONSUL_PORT = config.getInt("consul.agent.port")

  val client = {
    val builder = new com.ning.http.client.AsyncHttpClientConfig.Builder()
    new play.api.libs.ws.ning.NingWSClient(builder.build())
  }

  val lngbkConsul = new Consul(CONSUL_IP, CONSUL_PORT, Option.empty, client)
}
