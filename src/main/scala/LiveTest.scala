import com.lngbk.commons.discovery.{ConfigClient, ServiceDiscoveryClient}

/**
  * Created by beolnix on 03/09/16.
  */
object LiveTest extends App {

//  ServiceDiscoveryClient.check()
  ServiceDiscoveryClient.register()

  while (true) {
    Thread.sleep(1000)
    println(".")
    ConfigClient.updateServicesList()
  }
}
