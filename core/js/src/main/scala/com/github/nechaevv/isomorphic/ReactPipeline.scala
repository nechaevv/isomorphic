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
      _ ← Stream.eval(eventStream.enqueue1(appStartEvent))
      eventDispatcher = {
        val dispatcher = new QueueEventDispatcher[AppEvent](eventStream)
        eventDispatcherCallback(dispatcher)
        dispatcher
      }
      events = eventStream.dequeue
      reducerOutput ← events.scan((initialState, appStartEvent, false))((acc: (AppState, AppEvent, Boolean), event: AppEvent) ⇒ {
        val (state, _, hasChanged) = acc
        if (event == appStartEvent) acc
        else {
          val reducer: AppState ⇒ AppState = if (stateReducer.isDefinedAt(event)) stateReducer(event) else s ⇒ s
          val newState = reducer(state)
          (newState, event, !(state eq newState))
        }
      })
      (state, event, hasChanged) = reducerOutput
      _ ← if (hasChanged) Stream.eval(IO {
        val reactComponent = appComponent(state, eventDispatcher)(ReactRenderer)
        ReactDOM.render(reactComponent, container)
      } ) else Stream.empty
      _ ← if (effects.isDefinedAt(event)) Stream.eval(IO { eventDispatcher.pipe(effects(event)(state)) }) else Stream.empty
    } yield ()
    stream.compile.drain
  }
}
