

name := "code-generator"

version := "0.2.1"

organization := "com.geishatokyo"

scalaVersion := "2.11.1"

crossScalaVersions := Seq("2.10.3","2.11.1")

resolvers ++= Seq(
  "takezoux2@github" at "http://takezoux2.github.com/maven/"
)

libraryDependencies ++= Seq(
  "com.geishatokyo" %% "scala-st4" % "4.0.8",
  "org.specs2" %% "specs2" % "2.3.13" % "test"
)

libraryDependencies <++= (scalaVersion)(sv => {
  sv match{
    case sv if sv.startsWith("2.11") => {
      Seq("org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.1")
    }
    case _ => {
      Seq()
    }
  }
})