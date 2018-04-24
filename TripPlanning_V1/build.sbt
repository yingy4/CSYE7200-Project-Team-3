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

libraryDependencies += "com.typesafe.play" %% "play-mailer" % "6.0.1"
libraryDependencies += "com.typesafe.play" %% "play-mailer-guice" % "6.0.1"


libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.41"
libraryDependencies += "org.playframework.anorm" %% "anorm" % "2.6.1"

libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % "test"
)

//libraryDependencies += "org.scala-js" %% "scalajs-library" % "0.6.20"



