package com.github.nechaevv.isomorphic

import cats.effect._
import fs2._
import fs2.concurrent.Queue

object EventReducerPipeline {
  def run[AppState <: AnyRef](render: EventDispatcher ⇒ AppState ⇒ Unit,
                              stateReducer: Any ⇒ AppState ⇒ AppState, effects: Any ⇒ AppState ⇒ EventStream, initialState: AppState)
                             (implicit concurrent: Concurrent[IO]): IO[Queue[IO, Any]] = for {
    eventStream ← Queue.unbounded[IO, Any]
    renderFn = render(eventStream)
    events = eventStream.dequeue.takeWhile(event ⇒ event != AppStopEvent, takeFailure = true)
    _ ← (for {
      reducerOutput ← events.scan[(AppState, Any, Boolean)]((initialState, AppStartEvent, true))((acc, event: Any) ⇒ {
        val (state, _, _) = acc
        if (event == AppStartEvent) acc
        else {
          val newState = stateReducer(event)(state)
          (newState, event, !(state eq newState))
        }
      })
      (state, event, hasChanged) = reducerOutput
      renderStream = if (hasChanged) Stream.eval(IO {
        println(s"Tick, event: $event")
        renderFn(state)
      }) else Stream.empty
      effectStream = Stream.eval(concurrent.start(
        effects(event)(state).through(eventStream.enqueue).compile.drain
          .handleErrorWith(err ⇒ IO(err.printStackTrace()))
      ))
      _ ← renderStream ++ effectStream
    } yield ()).compile.drain
  } yield eventStream
}

case object AppStartEvent
case object AppStopEvent