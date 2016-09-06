package com.lngbk.commons.discovery

import java.util.concurrent.atomic.AtomicBoolean

import com.google.common.net.HostAndPort
import com.lngbk.commons.config.ServiceIdentity
import org.slf4j.LoggerFactory


/**
  * Created by beolnix on 28/08/16.
  */
object ServiceDiscoveryClient {
  import ConsulClient.lngbkConsul

  // constants
  val SERVICE_REGISTRATION_TTL = 5L // service registration TTL in seconds
  val logger = LoggerFactory.getLogger(ServiceDiscoveryClient.getClass)

  // dependencies
  val serviceIdentity = ServiceIdentity.readFromConfig

  def register() = {
    lngbkConsul.register(
      serviceIdentity.serviceAkkaPort,
      HostAndPort.fromParts("google.com", serviceIdentity.serviceAkkaPort),
      SERVICE_REGISTRATION_TTL,
      serviceIdentity.serviceType.toString,
      serviceIdentity.serviceId); // registers with a TTL of 3 seconds

    lngbkConsul.pass(serviceIdentity.serviceId)

    RegistrationCheck.startPeriodicalRegistrationUpdate()
  }


}
