package com.lngbk.commons.service

import com.lngbk.commons.management.bootstrap.ServiceBootstrapDirector

/**
  * Created by beolnix on 10/09/16.
  */
class LngbkService(waitForDependencies: Boolean, register: Boolean) {
  import com.lngbk.api._
  ServiceBootstrapDirector.initService(waitForDependencies, register)

}