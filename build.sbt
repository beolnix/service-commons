name := "service-commons"

organization := "com.lngbk"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.8"

filterScalaLibrary := false // include scala library in output

dependencyDotFile := file("dependencies.dot") //render dot file to `./dependencies.dot`

libraryDependencies ++= {
  val akkaVersion = "2.4.7"
  Seq(
    "com.codacy" %% "scala-consul" % "2.0.2",

    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,

    "com.typesafe.akka" %%  "akka-testkit"                       % akkaVersion % "test",
    "org.scalatest"     %%  "scalatest"                          % "2.2.6"     % "test")
}


