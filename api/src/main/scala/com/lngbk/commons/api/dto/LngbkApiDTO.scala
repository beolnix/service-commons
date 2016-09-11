package com.lngbk.commons.api.dto

/**
  * Created by beolnix on 10/09/16.
  */
class LngbkRequest(requestUuid: String)
class LngbkResponse(errorCode: Option[String])

case class LngbkVersionRequest()

case class VersionDTO(major: Int, minor: Int, build: Int) extends Comparable[VersionDTO] {
  override def compareTo(o: VersionDTO): Int = {
    if (this.major < o.major) {
      -1
    } else if (this.major == o.major && this.minor < o.minor) {
      -1
    } else if (this.major == o.major && this.minor == o.minor) {
      0
    } else {
      1
    }
  }
}


case class LngbkVersionResponse(
                                var minCompatible: VersionDTO,
                                var current: VersionDTO
                               )