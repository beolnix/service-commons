package com.lngbk.commons.client

import consul.v1.common.Service
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future


/**
  * Created by beolnix on 28/08/16.
  */
class Router {
  import com.lngbk.commons.consul.ConsulClient.lngbkConsul.v1._

  val servicesFuture:Future[Map[ServiceId,Service]] = agent.services()

  servicesFuture.map(services => {

  })
}
