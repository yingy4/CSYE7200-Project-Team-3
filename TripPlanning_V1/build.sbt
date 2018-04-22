name := "TripPlanning_V1"
 
version := "1.0" 
      
lazy val `tripplanning_v1` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq(evolutions,jdbc , ehcache , ws , specs2 % Test , guice )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )


libraryDependencies ++= Seq(
  ws
)
libraryDependencies += "com.typesafe.play" %% "play-ws" % "2.6.3"
libraryDependencies += "io.spray" %%  "spray-json" % "1.3.3"


libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.41"
libraryDependencies += "org.playframework.anorm" %% "anorm" % "2.6.1"
