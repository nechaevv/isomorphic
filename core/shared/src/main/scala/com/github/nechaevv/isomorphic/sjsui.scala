package com.github.nechaevv

package object isomorphic {
  type Reducer[E, S] = PartialFunction[E, S ⇒ S]
  type Effect[E] = PartialFunction[E, Iterable[E]]
}
