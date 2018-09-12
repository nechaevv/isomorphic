import org.scalajs.core.tools.linker.backend.OutputMode.{ECMAScript51Global, ECMAScript51Isolated, ECMAScript6}
import sbtcrossproject.CrossPlugin.autoImport.crossProject

val sharedSettings = Seq(
  organization := "com.github.nechaevv.sjs-ui",
  version := "0.1",
  scalaVersion := "2.12.6"
)

val reactVersion = "16.5.0"

lazy val sjsUI = crossProject(JSPlatform, JVMPlatform).in(file("sjs-ui"))
  .settings(name := "sjs-ui", version := "0.1-SNAPSHOT")
  .settings(sharedSettings)
  .jvmSettings(
    libraryDependencies ++= Seq(
      "co.fs2" %% "fs2-core" % "1.0.0-M5",
      "org.typelevel" %% "cats-effect" % "1.0.0",
      "com.github.julien-truffaut" %% "monocle-core" % "1.5.1-cats"
    )
  )
  .jsSettings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.5",
      "co.fs2" %%% "fs2-core" % "1.0.0-M5",
      "org.typelevel" %%% "cats-effect" % "1.0.0",
      "com.github.julien-truffaut" %%% "monocle-core" % "1.5.1-cats"
    ),
    scalacOptions += "-P:scalajs:sjsDefinedByDefault"
  )

lazy val example = project.in(file("example"))
  .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
  .settings(sharedSettings ++ Seq(
    name := "example",
    scalaJSUseMainModuleInitializer := true,
    scalacOptions += "-P:scalajs:sjsDefinedByDefault",
    scalaJSOutputMode := ECMAScript6,
    npmDependencies in Compile ++= Seq("react" → reactVersion, "react-dom" → reactVersion),
    webpackBundlingMode := BundlingMode.LibraryOnly()
  ))
  .dependsOn(sjsUI.js)

