package com.lngbk.commons.discovery

import akka.actor.{ActorRef, ActorSystem, Address, Props}
import akka.util.Timeout
import com.lngbk.commons.discovery.HealthCheckActor.{Ping, Pong}
import com.lngbk.commons.discovery.consul.ConsulClient
import com.typesafe.config.ConfigFactory
import org.scalatest.{MustMatchers, WordSpec}

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.concurrent.{Await, Future}

/**
  * Created by beolnix on 08/09/16.
  */
class CommunicationSmokeTestMultiJvmProducerNode1 extends WordSpec with MustMatchers {
  "A producer node" should {

    "Send a ping to consumer and get an answer" in {
      var count = 0;
      var addresses: Set[Address] = Set[Address]()
      do {
        addresses = ConsulClient.getServiceAddresses("ConsumerService")
        Thread.sleep(1000)
        count += 1
      } while (addresses.size < 1 && count < 5)

      addresses must not be empty
      println(s"addresses: $addresses ; got in $count attempts" )

      val config = ConfigFactory.load("producer")
      val system = ActorSystem("LngbkSystem", config)

      implicit val timeout = Timeout(5 seconds)
      val router = LngbkRouter("ConsumerService", system, Props[HealthCheckActor])
      val response: Future[Any] = router ? Ping

      val result = Await.result(response, timeout.duration)
      val matchedResult = result match {
        case Pong => Option(Pong)
        case _ =>  Option.empty
      }
      println(matchedResult)

      matchedResult must not be empty
//      println(result)
    }

  }
}

class CommunicationSmokeTestMultiJvmConsumerNode2 extends WordSpec with MustMatchers {
  "A comsumer node" should {

    "Receive a ping from producer and send anser back" in {
      ConsulClient.register()
      val config = ConfigFactory.load("consumer")
      val system = ActorSystem("LngbkSystem", config)

      implicit val timeout = Timeout(5 seconds)
      val pingActor = system.actorOf(HealthCheckActor.props, "ConsumerService")

      println(s"GOT ACTOR $pingActor with path ${pingActor.path}")

      var count = 0
      while (count < 5) {
        Thread.sleep(1000)
        count += 1
      }
    }

  }
}
