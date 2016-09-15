package com.lngbk.commons.config

import com.typesafe.config.ConfigFactory
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by beolnix on 28/08/16.
  */

object Tag extends Enumeration {
  type Tag = Value
  val Processing = Value("Processing")
  val API = Value("Api")
  val Clustered = Value("Clustered")
  val BusinessLogic = Value("BusinessLogic")
}

case class DatabaseConfig(
                           hostname: String,
                           port: Int,
                           databaseName: String,
                           login: String,
                           password: String
                         )

case class ServiceIdentity(
                            serviceId: String,
                            serviceAkkaPort: Int,
                            serviceHTTPPort: Int,
                            serviceType: String,
                            serviceIp: String,
                            databaseConfig: Option[DatabaseConfig]
                          )

object ServiceIdentity {

  private val logger: Logger = LoggerFactory.getLogger(ServiceIdentity.getClass)

  val readFromConfig = {
    val config = ConfigFactory.load()
    val serviceAkkaPort = config.getInt("akka.remote.netty.tcp.port")
    val serviceHttpPort = config.getInt("service.http.port")
    val serviceType = config.getString("service.type")
    val serviceIp = config.getString("akka.remote.netty.tcp.hostname")

    var databaseConfig: Option[DatabaseConfig] = None
    val isMongoProvided = config.hasPath("mongo.hostname")
    if (isMongoProvided) {
      val databaseHost = config.getString("mongo.hostname")
      val databasePort = config.getInt("mongo.port")
      val databaseName = config.getString("mongo.database")
      val databaseLogin = config.getString("mongo.login")
      val databasePassword = config.getString("mongo.password")

      logger.info(s"Initialize database connection: $databaseHost:$databasePort/$databaseName")
      databaseConfig = Some(DatabaseConfig(databaseHost, databasePort, databaseName, databaseLogin, databasePassword))
    }


    val serviceId = s"$serviceType@$serviceIp:$serviceAkkaPort"
    ServiceIdentity(serviceId, serviceAkkaPort, serviceHttpPort, serviceType, serviceIp, databaseConfig)

  }
}
