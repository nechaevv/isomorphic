package com.github.nechaevv.pipeline

import cats.effect.IO
import fs2.concurrent.Queue

class QueueEventDispatcher[E](queue: Queue[IO, E]) extends EventDispatcher[E] {
  override def dispatch(event: E): Unit = queue.enqueue1(event).unsafeRunAsyncAndForget()
}
