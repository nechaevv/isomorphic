enablePlugins(ScalaJSPlugin)

name := "sjs-ui"

organization := "com.github.nechaevv"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.5",
  "co.fs2"       %%% "fs2-core"    % "0.10.5",
  "com.github.julien-truffaut" %%% "monocle-core" % "1.5.1-cats"
)

scalaJSUseMainModuleInitializer := true

scalacOptions += "-P:scalajs:sjsDefinedByDefault"
