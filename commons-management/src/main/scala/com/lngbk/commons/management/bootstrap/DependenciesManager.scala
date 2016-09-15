package com.lngbk.commons.management.bootstrap

import com.lngbk.commons.discovery.LngbkRouter
import org.slf4j.LoggerFactory

import scala.collection.mutable


/**
  * Created by beolnix on 09/09/16.
  */
object DependenciesManager {
  private val logger = LoggerFactory.getLogger(DependenciesManager.getClass)

  private val _dependencies = mutable.Set[LngbkRouter]()

  def checkIn(lngbkRouter: LngbkRouter): Unit = {
    logger.info(s"check-in new dependency $lngbkRouter")
    _dependencies += lngbkRouter
    logger.info(s"dependencies list: ${_dependencies}")
  }

  def dependencies = _dependencies.toSet

  def areDependenciesReady(): Boolean = {
    if (_dependencies.isEmpty) {
      logger.info("Dependencies are empty, readiness check failed.")
      false
    } else {
      val failedDeps = getFailedDependencies
      if (failedDeps.isEmpty) {
        logger.info(s"All dependencies are ready, readiness check passed: ${_dependencies}")
        true
      } else {
        logger.info(s"There are failed deps. Dependency check failed: $failedDeps")
        false
      }
    }
  }

  def getFailedDependencies: Set[LngbkRouter] = {
    _dependencies.filter(!_.isReady).toSet
  }
}
