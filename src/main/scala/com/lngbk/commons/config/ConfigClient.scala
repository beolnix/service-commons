package com.lngbk.commons.config

import consul.v1.catalog.NodeProvidingService
import consul.v1.common.Node
import consul.v1.common.Types.ConsulResponseParseException

import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable
import scala.collection.immutable
import scala.concurrent.Future
import scala.util.control.NonFatal

/**
  * Created by beolnix on 28/08/16.
  */
object ConfigClient {

  import com.lngbk.commons.consul.ConsulClient.lngbkConsul.v1._
  var nodesMap = immutable.Map[String, List[String]]()

  private def init() = {
    val nodes: Future[Seq[Node]] = catalog.nodes()
    nodes.recover {
      case ConsulResponseParseException(jsError) => //do something with the JsError
      case NonFatal(otherException) => //something else
    }

    nodes.map(nodesList => {
      val newNodesMap = mutable.HashMap[String, List[String]]()
      nodesList.foreach(node => {
        val nodesList = newNodesMap.getOrElse(node.Node, List[String]())
        nodesList ++ node.Address
        newNodesMap.put(node.Node, nodesList)
      })

      nodesMap = newNodesMap.toMap  //make immutable
    })
  }

  def check() {
    print("registered!")


    while (true) {
      val services: Future[Map[ServiceType, Set[String]]] = catalog.services()
      services.recover {
        case ConsulResponseParseException(jsError) => println("js error" + jsError)
        case NonFatal(otherException) => {
          println("other error: " + otherException)
          otherException.printStackTrace()
        }
      }

      //      services.flatMap(entry => entry._1).mapTo()

      services.map(servicesMap => {

        servicesMap.foreach(entry => {
          val serviceFuture: Future[Seq[NodeProvidingService]] = catalog.service(ServiceType(entry._1))
          serviceFuture.map(serviceList => {
            println("service info: " + serviceList)
          })
        })
      })
      Thread.sleep(10000)
      println("-------")
    }
  }

}
