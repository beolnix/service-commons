import sbt._
import Keys._
import com.typesafe.sbt.SbtMultiJvm
import com.typesafe.sbt.SbtMultiJvm.MultiJvmKeys.MultiJvm

object MyBuild extends Build {

  lazy val root = project.in(file(".")).aggregate(
    discovery,
    management,
    versioning,
    config,
    api
  )

  val projectVersion = "0.0.4-SNAPSHOT"

  val akkaVersion = "2.4.10"

  val organizationName = "com.lngbk"

  lazy val discovery = project
    .settings(SbtMultiJvm.multiJvmSettings: _*)
    .settings(
      organization := organizationName,

      name := "discovery",

      version := projectVersion,

      libraryDependencies ++= Seq(
        "com.orbitz.consul" % "consul-client" % "0.12.7",

        "com.typesafe.akka" %% "akka-actor" % akkaVersion,
        "com.typesafe.akka" %% "akka-remote" % akkaVersion,
        "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
        "com.typesafe.akka" %% "akka-multi-node-testkit" % akkaVersion,
        "ch.qos.logback" % "logback-classic" % "1.1.7",

        "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
        "org.scalatest" %% "scalatest" % "2.2.6" % "test"
      ),

      // make sure that MultiJvm test are compiled by the default test compilation
      compile in MultiJvm <<= (compile in MultiJvm) triggeredBy (compile in Test),
      // disable parallel tests
      parallelExecution in Test := false,
      // make sure that MultiJvm tests are executed by the default test target,
      // and combine the results from ordinary test and multi-jvm tests
      executeTests in Test <<= (executeTests in Test, executeTests in MultiJvm) map {
        case (testResults, multiNodeResults)  =>
          val overall =
            if (testResults.overall.id < multiNodeResults.overall.id)
              multiNodeResults.overall
            else
              testResults.overall
          Tests.Output(overall,
            testResults.events ++ multiNodeResults.events,
            testResults.summaries ++ multiNodeResults.summaries)
      },
      licenses := Seq(("CC0", url("http://creativecommons.org/publicdomain/zero/1.0")))


    ).configs(MultiJvm)
    .dependsOn(config)

  lazy val management = project
    .settings(
      organization := organizationName,

      version := projectVersion,

      libraryDependencies ++= Seq(
        "com.orbitz.consul" % "consul-client" % "0.12.7",

        "com.typesafe.akka" %% "akka-actor" % akkaVersion,
        "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
        "ch.qos.logback" % "logback-classic" % "1.1.7",

        "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
        "org.scalatest" %% "scalatest" % "2.2.6" % "test"
      )
    ).dependsOn(discovery)

  lazy val versioning = project
    .settings(
      organization := organizationName,

      version := projectVersion,

      libraryDependencies ++= Seq(
        "com.orbitz.consul" % "consul-client" % "0.12.7",

        "com.typesafe.akka" %% "akka-actor" % akkaVersion,
        "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
        "ch.qos.logback" % "logback-classic" % "1.1.7",

        "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
        "org.scalatest" %% "scalatest" % "2.2.6" % "test"
      )
    )

  lazy val config = project
    .settings(
      organization := organizationName,

      version := projectVersion,

      libraryDependencies ++= Seq(
        "com.orbitz.consul" % "consul-client" % "0.12.7",

        "com.typesafe.akka" %% "akka-actor" % akkaVersion,
        "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
        "ch.qos.logback" % "logback-classic" % "1.1.7",

        "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
        "org.scalatest" %% "scalatest" % "2.2.6" % "test"
      )
    )

  lazy val api = project
    .settings(
      organization := organizationName,

      version := projectVersion
    ).dependsOn(management)
}
