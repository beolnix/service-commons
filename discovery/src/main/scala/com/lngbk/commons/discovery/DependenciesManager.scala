package com.lngbk.commons.discovery

import scala.collection.mutable

/**
  * Created by beolnix on 09/09/16.
  */
object DependenciesManager {
  private val _dependencies = mutable.Set[LngbkRouter]()

  def checkIn(lngbkRouter: LngbkRouter): Unit = {
    _dependencies += lngbkRouter
  }

  def areDependenciesReady(): Boolean = {
    if (_dependencies.isEmpty)
      false
    else
      _dependencies.map(_.isReady).exists(!_)
  }
}
