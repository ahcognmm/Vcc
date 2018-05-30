name := "rongbay_muaban"

version := "0.1"

scalaVersion := "2.12.6"

crossPaths := false

val workaround = {
    sys.props += "packaging.type" -> "jar"
    ()
}

libraryDependencies ++= Seq(
    "org.scala-lang.modules" % "scala-parser-combinators_2.12" % "1.0.5",
    "org.eclipse.jetty" % "jetty-servlet" % "9.4.8.v20171121",
    "javax.ws.rs" % "javax.ws.rs-api" % "2.1",
    "javax.servlet" % "javax.servlet-api" % "4.0.0",
    "org.glassfish.jersey.containers" % "jersey-container-servlet" % "2.26-b03",
    "org.glassfish.jersey.core" % "jersey-server" % "2.26-b03",
    "org.apache.commons" % "commons-lang3" % "3.1")