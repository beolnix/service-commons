package com.lngbk.commons.service

import com.lngbk.commons.management.bootstrap.ServiceBootstrapDirector

/**
  * Created by beolnix on 10/09/16.
  */
abstract class LngbkService(waitForDependencies: Boolean, register: Boolean) {

  initDependencies()
  ServiceBootstrapDirector.initService(waitForDependencies, register)

  def initDependencies(): Unit
}