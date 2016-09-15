package com.lngbk.commons.datastore

import com.lngbk.commons.config.ServiceIdentity
import org.slf4j.{Logger, LoggerFactory}
import reactivemongo.api.{MongoConnection, MongoDriver}

import scala.concurrent.Future

object LngbkDB {
  private val logger: Logger = LoggerFactory.getLogger(LngbkDB.getClass)

  private val config = ServiceIdentity.readFromConfig

  if (config.databaseConfig.isEmpty) {
    logger.error(s"Database connection hasn't been configured properly: ${config.databaseConfig}")
    throw new IllegalArgumentException("Database configuration is incorrect")
  }

  private val host = config.databaseConfig.map(c => c.hostname).getOrElse("localhost")
  private val port = config.databaseConfig.map(c => c.port).getOrElse(27017)
  private val database = config.databaseConfig.map(c => c.databaseName).getOrElse("mydb")
  private val login = config.databaseConfig.map(c => c.login).getOrElse("guest")
  private val password = config.databaseConfig.map(c => c.password).getOrElse("password")

  import reactivemongo.core.nodeset.Authenticate

  private val mongoUri = s"mongodb://$login:$password@$host:$port/$database"
  logger.info(s"Connect to the database using URI: $mongoUri")

  private val driver = MongoDriver()
  private val parsedUri = MongoConnection.parseURI(mongoUri)
  private val connection = parsedUri.map(driver.connection(_))
  val futureConnection = Future.fromTry(connection)
}
