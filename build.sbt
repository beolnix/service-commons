name := "service-commons"

organization := "com.lngbk"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.8"

filterScalaLibrary := false // include scala library in output

dependencyDotFile := file("dependencies.dot") //render dot file to `./dependencies.dot`
