package com.github.nechaevv.isomorphic

trait EventDispatcher[E] {
  def dispatch(event: E): Unit
}
