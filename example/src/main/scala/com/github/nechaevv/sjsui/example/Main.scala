package com.github.nechaevv.sjsui.example

import cats.effect._
import cats.syntax.all._
import com.github.nechaevv.sjsui.dom.CustomElementRegistry
import com.github.nechaevv.sjsui.{ReactPipeline, Reducer, WebComponent}
import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js
/*
object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    val reactContainer = document.getElementById("ReactContainer")
    /*
    ReactPipeline.run(reactContainer, AppComponent, stateReducer, initialState, AppStartEvent)
      .as(ExitCode.Success)
      */


  }
}

*/

object Main {

  def main(args: Array[String]): Unit = {
    CustomElementRegistry.register("app-tasks" → js.constructorOf[TasksApp])
  }
}