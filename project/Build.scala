import sbt._
import Keys._

object MyBuild extends Build {
  
  lazy val root = project.in(file(".")).aggregate(discovery, management, versioning)

  val akkaVersion = "2.4.7"

  lazy val discovery = project
      .settings(
    libraryDependencies ++= Seq(
    "com.orbitz.consul"  % "consul-client" % "0.12.7",

    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "ch.qos.logback" % "logback-classic" % "1.1.7",

    "com.typesafe.akka" %%  "akka-testkit"  % akkaVersion % "test",
    "org.scalatest"     %%  "scalatest" % "2.2.6"     % "test")


      )

  lazy val management = project

  lazy val versioning = project
}
