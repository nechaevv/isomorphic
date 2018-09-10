package com.github.nechaevv.sjsui.example

import cats.effect._
import cats.syntax.all._
import com.github.nechaevv.sjsui.ReactApp
import org.scalajs.dom.document

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    val reactContainer = document.getElementById("ReactContainer")
    ReactApp.run(reactContainer, AppComponent, stateReducer, initialState, AppStartEvent)
      .as(ExitCode.Success)
  }
}
