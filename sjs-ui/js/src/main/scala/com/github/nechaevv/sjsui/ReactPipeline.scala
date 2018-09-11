package com.github.nechaevv.sjsui

import cats.effect._
import cats.syntax.all._
import com.github.nechaevv.sjsui.react.{ReactDOM, ReactRenderer}
import fs2._
import fs2.concurrent.Queue
import org.scalajs.dom.{Element, Node}

object ReactPipeline {
  def run[AppEvent, AppState, AppComponent <: platform.Component[AppState, AppEvent]]
    (container: Node, appComponent: AppComponent, stateReducer: Reducer[AppEvent, AppState], initialState: AppState,
     appStartEvent: AppEvent, eventDispatcherCallback: EventDispatcher[AppEvent] ⇒ Unit)(implicit concurrent: Concurrent[IO]): IO[Unit] = {
    val stream = for {
      eventStream ← Stream.eval(Queue.unbounded[IO, AppEvent])
      _ ← Stream.eval(eventStream.enqueue1(appStartEvent))
      eventDispatcher = {
        val dispatcher = new QueueEventDispatcher[AppEvent](eventStream)
        eventDispatcherCallback(dispatcher)
        dispatcher
      }
      events = eventStream.dequeue
      state ← events.scan(initialState)((state, event) ⇒ {
        val reducer: AppState ⇒ AppState = if (stateReducer.isDefinedAt(event)) stateReducer(event) else s ⇒ s
        reducer(state)
      })
      _ ← Stream.eval(IO {
        val reactComponent = appComponent(state, eventDispatcher)(ReactRenderer)
        ReactDOM.render(reactComponent, container)
      } )
    } yield ()
    stream.compile.drain
  }
}
