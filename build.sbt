import org.scalajs.core.tools.linker.backend.OutputMode
import sbtcrossproject.CrossPlugin.autoImport.crossProject

val sharedSettings = Seq(
  organization := "com.github.nechaevv.isomorphic",
  version := "0.1-SNAPSHOT",
  scalaVersion := "2.12.6"
)

val reactVersion = "16.5.0"

lazy val core = crossProject(JSPlatform, JVMPlatform).in(file("core"))
  .settings(name := "isomorphic-core", version := "0.1-SNAPSHOT")
  .settings(sharedSettings)
  .jvmSettings(
    libraryDependencies ++= Seq(
      "co.fs2" %% "fs2-core" % "1.0.0-M5",
      "org.typelevel" %% "cats-effect" % "1.0.0",
    )
  )
  .jsSettings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.5",
      "co.fs2" %%% "fs2-core" % "1.0.0-M5",
      "org.typelevel" %%% "cats-effect" % "1.0.0",
    ),
    npmDependencies in Compile ++= Seq("react" → reactVersion, "react-dom" → reactVersion),
    scalacOptions += "-P:scalajs:sjsDefinedByDefault"
  )
  .jsConfigure(_.enablePlugins(ScalaJSBundlerPlugin))

val monocleVersion = "1.5.1-cats"
val circeVersion = "0.9.3"

lazy val example = project.in(file("example"))
  .enablePlugins(ScalaJSPlugin)
  .settings(sharedSettings ++ Seq(
    name := "example",
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-core" % circeVersion,
      "io.circe" %%% "circe-generic" % circeVersion,
      "io.circe" %%% "circe-parser" % circeVersion,
      "com.github.julien-truffaut" %%%  "monocle-core"  % monocleVersion,
      "com.github.julien-truffaut" %%%  "monocle-macro" % monocleVersion,
      "com.github.julien-truffaut" %%%  "monocle-law"   % monocleVersion % "test"
    ), 
    scalacOptions ++= Seq("-P:scalajs:sjsDefinedByDefault", "-feature"),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
    scalaJSOutputMode := OutputMode.ECMAScript6,
    scalaJSModuleKind := ModuleKind.CommonJSModule
  ))
  .dependsOn(core.js)

