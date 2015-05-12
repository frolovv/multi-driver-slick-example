name := "multi-driver-slick-example"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "org.specs2" %% "specs2-core" % "3.6" % "test",
  "com.h2database" % "h2" % "1.3.166" % "test",
  "org.xerial" % "sqlite-jdbc" % "3.7.2" % "test"
)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

scalacOptions in Test ++= Seq("-Yrangepos")
