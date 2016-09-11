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
  }

  def dependencies = _dependencies.toSet

  def areDependenciesReady(): Boolean = {
    if (_dependencies.isEmpty)
      false
    else _dependencies.map(!_.isReady).isEmpty
  }

  def getFailedDependencies: Set[LngbkRouter] = {
    _dependencies.filter(!_.isReady).toSet
  }
}
