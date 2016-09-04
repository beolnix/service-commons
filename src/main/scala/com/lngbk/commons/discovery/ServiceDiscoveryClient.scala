package com.lngbk.commons.discovery

import java.util.concurrent.atomic.AtomicBoolean

import com.lngbk.commons.config.dto.ServiceIdentity
import org.slf4j.LoggerFactory


/**
  * Created by beolnix on 28/08/16.
  */
object ServiceDiscoveryClient {
  import ConsulClient.lngbkConsul

  // constants
  val SERVICE_REGISTRATION_TTL = 3L // service registration TTL in seconds
  val logger = LoggerFactory.getLogger(ServiceDiscoveryClient.getClass)

  // dependencies
  val serviceIdentity = ServiceIdentity.readFromConfig

  def register() = {
    lngbkConsul.register(
      serviceIdentity.serviceAkkaPort,
      SERVICE_REGISTRATION_TTL,
      serviceIdentity.serviceType.toString,
      serviceIdentity.serviceId); // registers with a TTL of 3 seconds

    lngbkConsul.pass(serviceIdentity.serviceId)

    RegistrationCheck.startPeriodicalRegistrationUpdate()
  }


}
