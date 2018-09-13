package com.github.nechaevv.isomorphic

import cats.effect._
import com.github.nechaevv.isomorphic.react.{ReactDOM, ReactRenderer}
import fs2._
import fs2.concurrent.Queue
import org.scalajs.dom.Node

object ReactPipeline {
  def run[AppEvent, AppState](container: Node, appComponent: platform.Component[AppState, AppEvent],
                              stateReducer: Reducer[AppEvent, AppState], initialState: AppState,
                              appStartEvent: AppEvent, eventDispatcherCallback: EventDispatcher[AppEvent] ⇒ Unit)
                             (implicit concurrent: Concurrent[IO]): IO[Unit] = {
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
