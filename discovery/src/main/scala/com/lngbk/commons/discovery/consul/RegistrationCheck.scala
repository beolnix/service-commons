package com.lngbk.commons.discovery.consul

import java.util.concurrent.atomic.AtomicBoolean

import com.lngbk.commons.config.ServiceIdentity
import org.slf4j.LoggerFactory

/**
  * Created by beolnix on 04/09/16.
  */
object RegistrationCheck {
  import ConsulClient.lngbkConsul

  // constants
  private val CHECK_INTERVAL = 1 * 1000 // service registration check interval in ms
  private val logger = LoggerFactory.getLogger(RegistrationCheck.getClass)

  // dependencies
  private val serviceIdentity = ServiceIdentity.readFromConfig

  // state
  private val failRegistrationFlag: AtomicBoolean = new AtomicBoolean(false)
  private var currentCheck: Option[Thread] = Option.empty

  def failRegistration(): Unit = {
    failRegistrationFlag.set(true)
    if (currentCheck.isDefined) {
      currentCheck.get.interrupt()
      currentCheck = Option.empty
    }
    lngbkConsul.fail(serviceIdentity.serviceId)
  }

  def startPeriodicalRegistrationUpdate() = {
    failRegistrationFlag.set(false)
    if (currentCheck.isDefined) {
      currentCheck.get.interrupt()
    }

    currentCheck = Some(createNewThreadCheck())
    currentCheck.get.start()
  }

  private def createNewThreadCheck(): Thread = {
    val check = new Runnable {
      override def run(): Unit = {
        var stop = false
        while (!stop) {
          if (!failRegistrationFlag.get()) {
            lngbkConsul.pass(serviceIdentity.serviceId)
            logger.trace("pass service registration")
          } else {
            logger.warn("Fail Registration is true, failing service registration.")
            lngbkConsul.fail(serviceIdentity.serviceId)
          }
          try {
            Thread.sleep(CHECK_INTERVAL)
          } catch {
            case ioe: InterruptedException => stop = true;
          }
        }
      }
    }

    new Thread(check)
  }

}
