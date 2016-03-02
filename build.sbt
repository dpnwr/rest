import com.github.play2war.plugin.{Play2WarPlugin, Play2WarKeys}

name := """freeacs-rest-play"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.38"

Play2WarPlugin.play2WarSettings

Play2WarKeys.servletVersion := "3.1"

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
