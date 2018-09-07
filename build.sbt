enablePlugins(ScalaJSPlugin)

name := "sjs-ui"

organization := "com.github.nechaevv"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.5",
  "co.fs2"       %%% "fs2-core"    % "1.0.0-M5",
  "org.typelevel" %%% "cats-effect" % "1.0.0",
  "com.github.julien-truffaut" %%% "monocle-core" % "1.5.1-cats"
)

scalaJSUseMainModuleInitializer := true

scalacOptions += "-P:scalajs:sjsDefinedByDefault"
