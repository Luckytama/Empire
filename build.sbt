name          := """htwg-scala-empire"""
organization  := "de.htwg.se"
version       := "0.0.1"
scalaVersion  := "2.12.7"
scalacOptions := Seq("-unchecked", "-feature", "-deprecation", "-encoding", "utf8")

resolvers += Resolver.jcenterRepo

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

libraryDependencies += "junit" % "junit" % "4.8" % "test"
libraryDependencies ++= Seq(
  "org.apache.logging.log4j" %% "log4j-api-scala" % "11.0",
  "org.apache.logging.log4j" % "log4j-api" % "2.11.0",
  "org.apache.logging.log4j" % "log4j-core" % "2.11.0" % Runtime
)
libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.5.0.RC1"
libraryDependencies += "com.google.inject" % "guice" % "4.1.0"
libraryDependencies += "net.codingwell" %% "scala-guice" % "4.1.0"
libraryDependencies += "org.scala-lang.modules" % "scala-swing_2.12" % "2.0.3"

coverageExcludedPackages := "de\\.htwg\\.se\\.empire\\.view.*;de\\.htwg\\.se\\.empire\\.Empire.*;de\\.htwg\\.se\\.empire\\.controller\\.impl\\.Events.*"

fork in run := true


fork in run := true

fork in run := true

fork in run := true