package com.github.nechaevv.isomorphic

import cats.effect._
import com.github.nechaevv.isomorphic.api.ReactDOM
import fs2._
import fs2.concurrent.Queue
import org.scalajs.dom.Node

object ReactPipeline {
  def run[AppState <: AnyRef](container: Node, appComponent: Component[AppState],
                              stateReducer: Reducer[AppState], effects: Effect[AppState], initialState: AppState,
                              eventDispatcherCallback: EventDispatcher ⇒ Unit)
                             (implicit concurrent: Concurrent[IO]): IO[Unit] = {
    val stream = for {
      eventStream ← Stream.eval(Queue.unbounded[IO, Any])
      eventDispatcher = {
        val dispatcher = new QueueEventDispatcher(eventStream)
        eventDispatcherCallback(dispatcher)
        dispatcher
      }
      events = eventStream.dequeue.takeWhile(event ⇒ event != AppStopEvent, takeFailure = true)
      reducerOutput ← events.scan[(AppState, Any, Boolean)]((initialState, AppStartEvent, true))((acc, event: Any) ⇒ {
        val (state, _, _) = acc
        if (event == AppStartEvent) acc
        else if (stateReducer.isDefinedAt(event)) {
          val newState = stateReducer(event)(state)
          (newState, event, !(state eq newState))
        }
        else  (state, event, false)
      })
      (state, event, hasChanged) = reducerOutput
      renderStream = if (hasChanged) Stream.eval(IO {
        val reactComponent = appComponent(state, eventDispatcher)(ReactRenderer)
        ReactDOM.render(reactComponent, container)
      } ) else Stream.empty
      effectStream = if (effects.isDefinedAt(event)) Stream.eval(concurrent.start(eventDispatcher.pipe(effects(event)(state)).compile.drain))
        else Stream.empty
      _ ← renderStream ++ effectStream
    } yield ()
    stream.compile.drain
  }
}

case object AppStartEvent
case object AppStopEvent