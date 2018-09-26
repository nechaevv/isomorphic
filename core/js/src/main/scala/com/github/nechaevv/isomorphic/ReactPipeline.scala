package com.github.nechaevv.isomorphic

import cats.effect._
import com.github.nechaevv.isomorphic.react.{ReactDOM, ReactRenderer}
import fs2._
import fs2.concurrent.Queue
import org.scalajs.dom.Node

object ReactPipeline {
  def run[AppEvent, AppState <: AnyRef](container: Node, appComponent: platform.Component[AppState, AppEvent],
                              stateReducer: Reducer[AppEvent, AppState], effects: Effect[AppEvent, AppState], initialState: AppState,
                              appStartEvent: AppEvent, eventDispatcherCallback: EventDispatcher[AppEvent] ⇒ Unit)
                             (implicit concurrent: Concurrent[IO]): IO[Unit] = {
    val stream = for {
      eventStream ← Stream.eval(Queue.unbounded[IO, AppEvent])
      eventDispatcher = {
        val dispatcher = new QueueEventDispatcher[AppEvent](eventStream)
        eventDispatcherCallback(dispatcher)
        dispatcher
      }
      events = eventStream.dequeue
      reducerOutput ← events.scan((initialState, appStartEvent, true))((acc: (AppState, AppEvent, Boolean), event: AppEvent) ⇒ {
        val (state, _, _) = acc
        if (event == appStartEvent) acc
        else if (stateReducer.isDefinedAt(event)) {
          val newState = stateReducer(event)(state)
          (newState, event, !(state eq newState))
        }
        else  (state, event, false)
      })
      (state, event, hasChanged) = reducerOutput
      _ ← if (hasChanged) Stream.eval(IO {
        val reactComponent = appComponent(state, eventDispatcher)(ReactRenderer)
        ReactDOM.render(reactComponent, container)
      } ) else Stream.empty
      _ ← if (effects.isDefinedAt(event)) Stream.eval(concurrent.start(eventDispatcher.pipe(effects(event)(state)).compile.drain))
        else Stream.empty
    } yield ()
    stream.compile.drain
  }
}
