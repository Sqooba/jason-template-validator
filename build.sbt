organization := "io.sqooba.json"
scalaVersion := "2.12.5"
version      := "0.2.1-SNAPSHOT"
name         := "json-template-validator"

libraryDependencies ++= Seq(
  "com.fasterxml.jackson.core"  %   "jackson-core"            % "2.9.5",
  "com.fasterxml.jackson.core"  %   "jackson-databind"        % "2.9.5",
  "ch.qos.logback"              %   "logback-classic"         % "1.2.3",
  "com.typesafe.scala-logging"  %%  "scala-logging"           % "3.7.2",
  "org.scalatest"               %%  "scalatest"               % "3.0.4"             % Test,
  "org.mockito"                 %   "mockito-all"             % "1.10.19"           % Test
)

excludeDependencies ++= Seq("org.slf4j" % "slf4j-log4j12", "log4j" % "log4j")

val artUser = sys.env.get("ARTIFACTORY_CREDS_USR").getOrElse("")
val artPass = sys.env.get("ARTIFACTORY_CREDS_PSW").getOrElse("")

credentials += Credentials("Artifactory Realm", "artifactory-v2.sqooba.io", artUser, artPass)

publishTo := {
  val realm = "Artifactory Realm"
  val artBaseUrl = "https://artifactory-v2.sqooba.io/artifactory"
  if (isSnapshot.value) {
    Some(realm at s"$artBaseUrl/libs-snapshot-local;build.timestamp=" + new java.util.Date().getTime)
  } else {
    Some(realm at s"$artBaseUrl/libs-release-local")
  }
}
