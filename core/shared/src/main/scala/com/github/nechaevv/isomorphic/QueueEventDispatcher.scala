package com.github.nechaevv.isomorphic

import cats.effect.IO
import fs2.concurrent.Queue

class QueueEventDispatcher[E](queue: Queue[IO, E]) extends EventDispatcher[E] {

  override def dispatch(event: E): Unit = queue.enqueue1(event).unsafeRunAsyncAndForget()

  override def pipe(events: fs2.Stream[IO, E]): Unit = events.through(queue.enqueue).compile.drain.unsafeRunAsyncAndForget()

}
