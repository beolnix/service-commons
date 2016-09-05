package com.lngbk.commons.versioning

import com.typesafe.config.ConfigFactory

/**
  * Created by beolnix on 04/09/16.
  */
object VersionProvider {
  val config = ConfigFactory.load()
  def resolveVer(serviceType: String) = config.getString(s"$serviceType.api.version")
}
