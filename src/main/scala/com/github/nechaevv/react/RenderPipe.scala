package com.github.nechaevv.react

import cats.effect.IO
import com.github.nechaevv.core.Component
import fs2.Sink
import org.scalajs.dom.Element

class RenderPipe[S](viewport: Element, component: Component[S]) extends Sink[IO, S] {
  override def apply(s: fs2.Stream[IO, S]): fs2.Stream[IO, Unit] = s evalMap { state ⇒
    IO.delay(ReactDOM.render(component(state)(ReactRenderer), viewport))
  }
}
