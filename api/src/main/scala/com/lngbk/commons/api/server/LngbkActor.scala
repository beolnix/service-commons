package com.lngbk.commons.api.server

import akka.actor.Actor
import com.lngbk.commons.api.dto.{LngbkVersionRequest, LngbkVersionResponse, VersionDTO}
import com.lngbk.commons.api.util.VersionHelper

/**
  * Created by beolnix on 10/09/16.
  */
abstract class LngbkActor extends Actor {
  override def receive: Receive = {
    case LngbkVersionRequest(requestUuid) => LngbkVersionResponse(
      VersionHelper.minCompatibleVersion, VersionHelper.currentVersion, Option.empty
    )
    case _ => process
  }

  def process: Receive
}
