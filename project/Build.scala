import sbt._
import Keys._
import com.typesafe.sbt.SbtMultiJvm
import com.typesafe.sbt.SbtMultiJvm.MultiJvmKeys.MultiJvm

object MyBuild extends Build {

  lazy val root = project.in(file(".")).aggregate(
    discovery,
    management,
    config,
    api,
    service,
    datastore
  )

  val projectVersion = "0.0.37-SNAPSHOT"

  val akkaVersion = "2.4.10"
  val scalaTestVersion = "2.2.6"
  val logbackVersion = "1.1.7"
  val consulClientVersion = "0.12.7"
  val reactiveMongoVersion = "0.11.14"

  val organizationName = "com.lngbk"

  lazy val discovery = project
    .in(file("commons-discovery"))
    .settings(SbtMultiJvm.multiJvmSettings: _*)
    .settings(
      organization := organizationName,

      name := "commons-discovery",

      artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
        name.toString + "-" + module.revision + "." + artifact.extension
      },

      version := projectVersion,

      libraryDependencies ++= Seq(
        "com.orbitz.consul" % "consul-client" % consulClientVersion,

        "com.typesafe.akka" %% "akka-actor" % akkaVersion,
        "com.typesafe.akka" %% "akka-remote" % akkaVersion,
        "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
        "com.typesafe.akka" %% "akka-multi-node-testkit" % akkaVersion,
        "ch.qos.logback" % "logback-classic" % logbackVersion,

        "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
        "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
      ),

      // make sure that MultiJvm test are compiled by the default test compilation
      compile in MultiJvm <<= (compile in MultiJvm) triggeredBy (compile in Test),
      // disable parallel tests
      parallelExecution in Test := false,
      // make sure that MultiJvm tests are executed by the default test target,
      // and combine the results from ordinary test and multi-jvm tests
      executeTests in Test <<= (executeTests in Test, executeTests in MultiJvm) map {
        case (testResults, multiNodeResults) =>
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
    .in(file("commons-management"))
    .settings(
      organization := organizationName,

      name := "commons-management",

      artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
        name.toString + "-" + module.revision + "." + artifact.extension
      },

      version := projectVersion,

      libraryDependencies ++= Seq(
        "com.orbitz.consul" % "consul-client" % consulClientVersion,

        "com.typesafe.akka" %% "akka-actor" % akkaVersion,
        "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
        "ch.qos.logback" % "logback-classic" % logbackVersion,

        "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
        "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
      )
    ).dependsOn(discovery)

  lazy val datastore = project
    .in(file("commons-datastore"))
    .settings(
      organization := organizationName,

      name := "commons-datastore",

      artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
        name.toString + "-" + module.revision + "." + artifact.extension
      },

      version := projectVersion,

      libraryDependencies ++= Seq(
        "org.reactivemongo" %% "reactivemongo" % reactiveMongoVersion,

        "ch.qos.logback" % "logback-classic" % logbackVersion,

        "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
      )
    ).dependsOn(discovery, config)

  lazy val config = project
    .in(file("commons-config"))
    .settings(
      organization := organizationName,

      name := "commons-config",

      artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
        name.toString + "-" + module.revision + "." + artifact.extension
      },

      version := projectVersion,

      libraryDependencies ++= Seq(
        "com.orbitz.consul" % "consul-client" % consulClientVersion,

        "com.typesafe.akka" %% "akka-actor" % akkaVersion,
        "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
        "ch.qos.logback" % "logback-classic" % logbackVersion,

        "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
        "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
      )
    )

  lazy val api = project
    .in(file("commons-api"))
    .settings(
      organization := organizationName,

      name := "commons-api",

      artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
        name.toString + "-" + module.revision + "." + artifact.extension
      },

      version := projectVersion
    ).dependsOn(management)

  lazy val service = project
    .in(file("commons-service"))
    .settings(
      organization := organizationName,

      name := "commons-service",

      artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
        name.toString + "-" + module.revision + "." + artifact.extension
      },

      version := projectVersion
    ).dependsOn(management)
}
