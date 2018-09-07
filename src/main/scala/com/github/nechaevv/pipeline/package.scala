package com.github.nechaevv

package object pipeline {
  type Reducer[S] = PartialFunction[Event, S ⇒ S]
  type Effect = PartialFunction[Event, Iterable[Event]]
}
