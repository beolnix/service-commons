package com.lngbk.commons.management.bootstrap

import com.lngbk.commons.discovery.LngbkRouter

import scala.collection.mutable


/**
  * Created by beolnix on 09/09/16.
  */
object DependenciesManager {
  private val _dependencies = mutable.Set[LngbkRouter]()

  def checkIn(lngbkRouter: LngbkRouter): Unit = {
    _dependencies += lngbkRouter
  }

  def dependencies = _dependencies.toSet

  def areDependenciesReady(): Boolean = {
    if (_dependencies.isEmpty)
      false
    else
      _dependencies.map(_.isReady).exists(!_)
  }

  def getFailedDependencies: Set[LngbkRouter] = {
    _dependencies.filter(!_.isReady).toSet
  }
}
