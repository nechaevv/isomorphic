package com.github.nechaevv.isomorphic

import cats.effect.IO
import fs2.concurrent.Queue

class QueueEventDispatcher(queue: Queue[IO, Any]) extends EventDispatcher {

  override def dispatch(event: Any): Unit = queue.enqueue1(event).unsafeRunAsyncAndForget()

  override def pipe(events: fs2.Stream[IO, Any]): fs2.Stream[IO, Unit] = events.through(queue.enqueue)

}
