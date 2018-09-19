package com.github.nechaevv

import cats.effect.IO
import fs2.Stream

package object isomorphic {
  type Reducer[E, S] = PartialFunction[E, S ⇒ S]
  type Effect[E, S] = PartialFunction[E, S ⇒ Stream[IO, E]]
}
