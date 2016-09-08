package com.lngbk.commons.discovery.utils

import java.util.TimerTask

/**
  * Created by beolnix on 08/09/16.
  */
object DiscoveryUtils {
  implicit def function2TimerTask(f: () => Unit): TimerTask = {
    new TimerTask {
      def run() = f()
    }
  }

}
