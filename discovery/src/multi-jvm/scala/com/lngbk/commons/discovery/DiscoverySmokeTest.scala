package com.lngbk.commons.discovery

import com.lngbk.commons.discovery.consul.ConsulClient

/**
  * Created by beolnix on 08/09/16.
  */
object ServiceProducerMultiJvmProducerNode1 {
  while(true) {
    val addresses = ConsulClient.getServiceAddresses("ConsumerService")
    println("I see " + addresses.size + " consumer services")
    Thread.sleep(1000)
  }
}

object ServiceProducerMultiJvmConsumerNode2 {
  while(true) {
    val addresses = ConsulClient.getServiceAddresses("ProducerService")
    println("I see " + addresses.size + " consumer services")
    Thread.sleep(1000)
  }
}
