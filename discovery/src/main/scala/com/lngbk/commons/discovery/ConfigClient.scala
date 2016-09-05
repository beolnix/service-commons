package com.lngbk.commons.discovery

import com.orbitz.consul.model.health.ServiceHealth
import org.slf4j.LoggerFactory

import collection.JavaConversions._

/**
  * Created by beolnix on 28/08/16.
  */
object ConfigClient {
  import ConsulClient.healthClient

  //constants
  private val logger = LoggerFactory.getLogger(RegistrationCheck.getClass)

  def updateServicesList() {
    val nodes: Seq[ServiceHealth] = healthClient.getHealthyServiceInstances("ACTOR").getResponse(); // discover only "passing" nodes
    logger.info(" got " + nodes.size + " services from discovery")
    nodes.foreach(serviceHealth => {
      logger.info("id: " + serviceHealth.getService.getId + "; " +
        "service: " + serviceHealth.getService.getService + "; " +
        "address: " + serviceHealth.getService.getAddress + "; " +
        "port: " + serviceHealth.getService.getPort + "; " +
        "nodeAddress: " + serviceHealth.getNode.getAddress)
    })
  }

}
