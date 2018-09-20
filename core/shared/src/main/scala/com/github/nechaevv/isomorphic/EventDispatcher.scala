package com.github.nechaevv.isomorphic

import cats.effect.IO
import fs2.Stream

trait EventDispatcher[E] {
  def dispatch(event: E): Unit
  def pipe(events: Stream[IO, E]): Unit
}
