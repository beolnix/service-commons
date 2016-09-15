package com.lngbk.commons.api.util

import com.lngbk.commons.api.dto.VersionDTO
import com.typesafe.config.ConfigFactory

/**
  * Created by beolnix on 04/09/16.
  */
object VersionHelper {
  val serviceConfig = ConfigFactory.load("akka-application.conf")

  def apiVersion(serviceName: String): VersionDTO = {
    val apiConfig = ConfigFactory.load(s"$serviceName-api.conf")
    val versionStr = apiConfig.getString("api.version")
    resolveVer(versionStr)
  }

  val minCompatibleVersion: VersionDTO = {
    val versionStr = serviceConfig.getString("service.minCompatibleVersion")
    resolveVer(versionStr)
  }

  val currentVersion: VersionDTO = {
    val versionStr = serviceConfig.getString("service.version")
    resolveVer(versionStr)
  }

  private def resolveVer(versionStr: String): VersionDTO = {
    val versionParts = versionStr.split("\\.")

    val majorVer: Int = versionParts(0).toInt
    val minorVer: Int = versionParts(1).toInt
    val build: Int = versionParts(2).split("\\-")(0).toInt

    VersionDTO(majorVer, minorVer, build)
  }
}
