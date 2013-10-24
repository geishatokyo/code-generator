

name := "code-generator"

version := "0.1.1-SNAPSHOT"

organization := "com.geishatokyo"

scalaVersion := "2.10.0"

resolvers ++= Seq(
  "takezoux2@github" at "http://takezoux2.github.com/maven/"
)

libraryDependencies ++= Seq(
  "com.geishatokyo" %% "scala-st4" % "4.0.5-SNAPSHOT",
  "org.specs2" %% "specs2" % "1.12.3" % "test"
)