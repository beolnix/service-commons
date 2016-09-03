package com.lngbk.commons.config.dto

import com.lngbk.commons.config.dto.ServiceType.ServiceType
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

object ServiceType extends Enumeration {
  type ServiceType = Value
  val ACTOR = Value("ACTOR")
  val REST = Value("REST")
}

case class ServiceIdentity(serviceId: String, serviceAkkaPort: Int, serviceHTTPPort: Int, serviceType: ServiceType)

object ServiceIdentity {
  val readFromConfig = {
    val config = ConfigFactory.load()
    val serviceAkkaPort = config.getInt("service.akka.port")
    val serviceHttpPort = config.getInt("service.akka.port")
    val serviceId = config.getString("service.id")
    val serviceType = config.getString("service.type") match {
      case "ACTOR" => ServiceType.ACTOR
      case "REST" => ServiceType.REST
    }

    ServiceIdentity(serviceId, serviceAkkaPort, serviceHttpPort, serviceType)
  }
}
