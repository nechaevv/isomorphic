package com.github.nechaevv.pipeline

trait EventDispatcher[E] {
  def dispatch(event: E): Unit
}
