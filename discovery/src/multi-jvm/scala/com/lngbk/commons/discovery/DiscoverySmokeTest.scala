package com.lngbk.commons.discovery

import akka.actor.Address
import com.lngbk.commons.discovery.consul.{ConsulClient, RegistrationCheck}
import org.scalatest.WordSpec
import org.scalatest.MustMatchers

/**
  * Created by beolnix on 08/09/16.
  */
class DiscoverySmokeTestMultiJvmProducerNode1 extends WordSpec with MustMatchers {
  "A producer node" should {

    "Get adresses list of consumer node" in {
      ConsulClient.register()

      var count = 0;
      var addresses: Set[Address] = Set[Address]()
      do {
        addresses = ConsulClient.getServiceAddresses("ConsumerService")
        Thread.sleep(1000)
        count += 1
      } while (addresses.size < 1 && count < 5)
      RegistrationCheck.failRegistration()
      

      addresses must not be empty
      println(s"addresses: $addresses ; got in $count attempts" )
    }
  }
}

class DiscoverySmokeTestMultiJvmConsumerNode2 extends WordSpec with MustMatchers {
  "A consumer node" should {
    "Get adresses list of producer node" in {
      ConsulClient.register()

      var count = 0;
      var addresses: Set[Address] = Set[Address]()
      do {
        addresses = ConsulClient.getServiceAddresses("ProducerService")
        Thread.sleep(1000)
        count += 1
      } while (addresses.size < 1 && count < 5)
      RegistrationCheck.failRegistration()

      addresses must not be empty
      println(s"addresses: $addresses ; got in $count attempts" )
    }
  }
}


