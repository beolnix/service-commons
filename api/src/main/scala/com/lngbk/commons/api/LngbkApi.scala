package com.lngbk.commons.api

import akka.actor.Props
import com.lngbk.commons.discovery.LngbkRouter
import com.lngbk.commons.management.SystemManager
import com.lngbk.commons.management.bootstrap.DependenciesManager

/**
  * Created by beolnix on 10/09/16.
  */
class LngbkApi(val serviceName: String, apiProps: Props) {

  private val _router: LngbkRouter = LngbkRouter(
    serviceName,
    SystemManager.system,
    apiProps
  )

  DependenciesManager.checkIn(_router)

  def router: LngbkRouter = _router
}
