package com.github.nechaevv.sjsui.example

import cats.effect._
import cats.syntax.all._
import com.github.nechaevv.sjsui.QueueEventDispatcher
import com.github.nechaevv.sjsui.react.{ReactDOM, ReactRenderer}
import fs2._
import fs2.concurrent.Queue
import org.scalajs.dom.document

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    val reactContainer = document.getElementById("ReactContainer")

    val stream = for {
      eventStream ← Stream.eval(Queue.unbounded[IO, AppEvent])
      _ ← Stream.eval(eventStream.enqueue1(AppStartEvent))
      eventDispatcher = new QueueEventDispatcher[AppEvent](eventStream)
      events = eventStream.dequeue
      state ← events.scan(initialState)((state, event) ⇒ {
        val reducer: TasksState ⇒ TasksState = if (stateReducer.isDefinedAt(event)) stateReducer(event) else s ⇒ s
        reducer(state)
      })
      _ ← Stream.eval(IO {
        val reactComponent = AppComponent(state, eventDispatcher)(ReactRenderer)
        ReactDOM.render(reactComponent, reactContainer)
      } )
    } yield ()
    stream.compile.drain.as(ExitCode.Success)

//    val reactComponent = AppComponent(state)(ReactRenderer)
//    val reactContainer = document.getElementById("ReactContainer")
//    ReactDOM.render(reactComponent, reactContainer)
  }
}
