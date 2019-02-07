package com.github.nechaevv

import cats.effect.IO
import fs2.Stream
import fs2.concurrent.Queue

import scala.language.implicitConversions

package object isomorphic {
  type Reducer[S] = PartialFunction[Any, S ⇒ S]
  type Effect[S] = PartialFunction[Any, S ⇒ ActionStream]
  type ActionStream = Stream[IO, Any]
  type ActionDispatcher = Queue[IO, Any]

  def combineReducers[S](reducers: Reducer[S]*): Any ⇒ S ⇒ S = event ⇒ state ⇒ {
    reducers.foldLeft(state)((s, reducer) ⇒ if (reducer.isDefinedAt(event)) reducer(event)(s) else s)
  }

  def combineEffects[S](effects: Effect[S]*): Any ⇒ S ⇒ ActionStream = event ⇒ state ⇒ {
    effects.foldLeft[fs2.Stream[IO, Any]](fs2.Stream.empty)((s, effect) ⇒ {
      if (effect.isDefinedAt(event)) s ++ effect(event)(state) else s
    })
  }

}
