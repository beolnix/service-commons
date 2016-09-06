package com.lngbk.commons.config

import com.typesafe.config.ConfigFactory

/**
  * Created by beolnix on 28/08/16.
  */

object Tag extends Enumeration {
  type Tag = Value
  val Processing = Value("Processing")
  val API = Value("Api")
  val Clustered = Value("Clustered")
  val BusinessLogic = Value("BusinessLogic")
}

case class ServiceIdentity(serviceId: String, serviceAkkaPort: Int, serviceHTTPPort: Int, serviceType: String)

object ServiceIdentity {
  val readFromConfig = {
    val config = ConfigFactory.load()
    val serviceAkkaPort = config.getInt("service.akka.port")
    val serviceHttpPort = config.getInt("service.akka.port")
    val serviceId = config.getString("service.id")
    val serviceType = config.getString("service.type")

    ServiceIdentity(serviceId, serviceAkkaPort, serviceHttpPort, serviceType)
  }
}
