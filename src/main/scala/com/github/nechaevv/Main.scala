package com.github.nechaevv

import cats.effect._
import cats.syntax.all._
import fs2._
import com.github.nechaevv.dom.DomRenderer
import com.github.nechaevv.pipeline.Event
import com.github.nechaevv.react.{ReactDOM, ReactRenderer}
import org.scalajs.dom.document
import com.github.nechaevv.tasks.{AppComponent, Task, TasksState}
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends IOApp {

  def run(args: Array[String]): IO[ExitCode] = {
    val state = TasksState(Seq(Task("Task 1", true), Task("Task 2", false)), Task("Task name", false))
    val stream = for {
      eventStream ← Stream.eval(async.topic[IO, Event])
    } yield ()
    stream.compile.drain.as(ExitCode.Success)

//    val reactComponent = AppComponent(state)(ReactRenderer)
//    val reactContainer = document.getElementById("ReactContainer")
//    ReactDOM.render(reactComponent, reactContainer)
  }
}
