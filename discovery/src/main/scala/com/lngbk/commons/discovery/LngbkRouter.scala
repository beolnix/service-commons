package com.lngbk.commons.discovery

import com.lngbk.commons.discovery.consul.ConsulClient

/**
  * Created by beolnix on 28/08/16.
  */
class LngbkRouter(val serviceName: String) {
  private var services = ConsulClient.getServiceAddresses(serviceName)
}

object LngbkRouter {
  def apply(serviceName: String) = new LngbkRouter(serviceName)

}
