enablePlugins(ScalaJSPlugin)

name := "icostack-ui"

organization := "io.icostack"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.5",
  "com.lihaoyi"  %%% "scalatags"   % "0.6.7",
  "co.fs2"       %%% "fs2-core"    % "0.10.5"
)

scalaJSUseMainModuleInitializer := true