package com.github.nechaevv.isomorphic

import cats.effect.IO
import fs2.Stream

trait EventDispatcher {
  def dispatch(event: Any): Unit
  def pipe(events: Stream[IO, Any]): fs2.Stream[IO, Unit]
}
