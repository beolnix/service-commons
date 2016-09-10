package com.lngbk.commons.management.bootstrap

import com.lngbk.commons.discovery.consul.ConsulClient
import org.slf4j.LoggerFactory

/**
  * Created by beolnix on 09/09/16.
  */
object ServiceBootstrapDirector {

  // constants
  private val logger = LoggerFactory.getLogger(ServiceBootstrapDirector.getClass)
  private val DEPENDENCIES_CHECK_DELAY = 1 * 1000 // 1s in ms
  private val DEPENDENCIES_CHECKS_LIMIT = 20

  def initService(waitForDependencies: Boolean = true, register: Boolean = true): Unit = {
    import com.lngbk.api._
    if (waitForDependencies) {
      initDependencies()
    } else {
      logger.info("Skip dependencies initialization because of the bootstrap configuraiton.")
    }

    if (register) {
      ConsulClient.register()
    } else {
      logger.info("Skip service registration in service registry because of the bootstrap configuraiton.")
    }
  }

  private def initDependencies(): Unit = {
    logger.info("Initializing dependencies...")
    var check = 0
    while (!DependenciesManager.areDependenciesReady() && check < DEPENDENCIES_CHECKS_LIMIT) {
      val failedDeps = DependenciesManager.getFailedDependencies
      logger.info(s"Got unsatisfied dependencies on $check, postpone bootstrap " +
        s"for $DEPENDENCIES_CHECK_DELAY ms: $failedDeps")
      Thread.sleep(DEPENDENCIES_CHECK_DELAY)
      check += 1
    }

    if (DependenciesManager.areDependenciesReady()) {
      logger.info("All dependencies are satisfied:" +
        s"${DependenciesManager.dependencies}")
    } else {
      val failedDeps = DependenciesManager.getFailedDependencies
      logger.error(s"Fail to satisfy service dependencies: $failedDeps, terminate service bootstrap.")
      System.exit(1)
    }

  }
}
