package com.lngbk.commons.api.dto

import java.util.UUID

class LngbkRequest(requestUuid: String) {
  def this() {
    this(UUID.randomUUID().toString)
  }
}
class LngbkResponse(errorCode: Option[String]) {
  def this() {
    this(None)
  }
}

case class LngbkVersionRequest(requestUuid: String) extends LngbkRequest(requestUuid) {
  def this() {
    this(UUID.randomUUID().toString)
  }
}

object LngbkVersionRequest {
  def apply() = new LngbkVersionRequest()
}


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
                                minCompatible: VersionDTO,
                                current: VersionDTO,
                                errorCode: Option[String]
                               ) extends LngbkResponse(errorCode) {
  def this() {
    this(null, null, None)
  }
}