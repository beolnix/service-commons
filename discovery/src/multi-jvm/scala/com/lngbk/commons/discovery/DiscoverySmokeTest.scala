package com.lngbk.commons.discovery

import com.lngbk.commons.discovery.consul.ConsulClient

/**
  * Created by beolnix on 08/09/16.
  */
object DiscoverySmokeTestMultiJvmProducerNode1 {
  def main(args: Array[String]): Unit = {
    ConsulClient.register()
    while(true) {
      val addresses = ConsulClient.getServiceAddresses("ConsumerService")
      println("I see " + addresses.size + " consumer services: " + addresses)
      Thread.sleep(1000)
    }
  }
}

object DiscoverySmokeTestMultiJvmConsumerNode2 {
  def main(args: Array[String]): Unit = {
    ConsulClient.register()
    while(true) {
      val addresses = ConsulClient.getServiceAddresses("ProducerService")
      println("I see " + addresses.size + " producer services: " + addresses)
      Thread.sleep(1000)
    }
  }
}
