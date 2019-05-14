name := """htwg-scala-empire"""
organization := "de.htwg.se"
version := "0.0.1"
scalaVersion := "2.11.8"
scalacOptions := Seq("-unchecked", "-feature", "-deprecation", "-encoding", "utf8")

resolvers += Resolver.jcenterRepo

libraryDependencies ++= {
  val scalaTestV = "3.0.1"
  val scalaMockV = "3.2.2"
  Seq(
    "org.scalatest" %% "scalatest" % scalaTestV % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % scalaMockV % "test"
  )
}

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.8" % "test",
  "org.apache.logging.log4j" % "log4j-api" % "2.11.0",
  "org.apache.logging.log4j" % "log4j-core" % "2.11.0",
  "org.json4s" %% "json4s-jackson" % "3.3.0",
  "net.codingwell" %% "scala-guice" % "4.2.1",
  "org.scala-lang" % "scala-swing" % "2.11+",
  "com.typesafe.akka" %% "akka-http"   % "10.1.8",
  "com.typesafe.akka" %% "akka-stream" % "2.5.19",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.8"
)

coverageExcludedPackages := "de\\.htwg\\.se\\.empire\\.view.*;de\\.htwg\\.se\\.empire\\.Empire.*;de\\.htwg\\.se\\.empire\\.controller\\.impl\\.Events.*"

fork in run := true
