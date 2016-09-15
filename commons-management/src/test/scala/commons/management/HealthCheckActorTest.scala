package commons.management

import akka.actor.ActorSystem
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit}
import com.lngbk.commons.management.HealthCheckActor
import commons.utils.StopSystemAfterAll
import org.scalatest.{MustMatchers, WordSpecLike}

/**
  * Created by beolnix on 28/08/16.
  */
class HealthCheckActorTest extends TestKit(ActorSystem("testService"))
  with WordSpecLike
  with MustMatchers
  with ImplicitSender
  with DefaultTimeout
  with StopSystemAfterAll {

  "HealthCheckActor must" must {

    "Reply on ping msg with pong msg" in {
      import com.lngbk.commons.management.HealthCheckActor._

      val healthCheckActorRef = system.actorOf(HealthCheckActor.props)
      healthCheckActorRef ! Ping
      expectMsg(Pong)
    }
  }

}
