package com.lngbk.commons.discovery


import com.lngbk.commons.config.dto.ServiceIdentity
import consul.v1.common.Node
import consul.v1.common.Types._
import consul.v1.agent.service.LocalService._
import consul.v1.catalog.NodeProvidingService
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.control.NonFatal

/**
  * Created by beolnix on 28/08/16.
  */
object ServiceDiscoveryClient {

  import com.lngbk.commons.consul.ConsulClient.lngbkConsul.v1._

  val serviceIdentity = ServiceIdentity.readFromConfig

  val myServiceTTLCheck = agent.service.ttlCheck(serviceIdentity.serviceId + " service status")
  val myService = consul.v1.agent.service.LocalService(
    ServiceId(serviceIdentity.serviceId),
    ServiceType(serviceIdentity.serviceType.toString),
    Set(ServiceTag("test")),
    Some(serviceIdentity.serviceAkkaPort),
    Option.empty,
    Some(Address("127.0.0.1"))
  )

  agent.service.register(myService)
}
