name := "service-commons"

scalacOptions := Seq("-unchecked", "-deprecation")

organization := "com.lngbk"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.8"

filterScalaLibrary := false // include scala library in output

dependencyDotFile := file("dependencies.dot") //render dot file to `./dependencies.dot`

resolvers += DefaultMavenRepository

libraryDependencies ++= {
  val akkaVersion = "2.4.7"
  Seq(
    "com.orbitz.consul"  % "consul-client" % "0.12.7",

    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "ch.qos.logback" % "logback-classic" % "1.1.7",

    "com.typesafe.akka" %%  "akka-testkit"  % akkaVersion % "test",
    "org.scalatest"     %%  "scalatest" % "2.2.6"     % "test")
}


