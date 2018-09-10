package com.github.nechaevv.sjsui

trait EventDispatcher[E] {
  def dispatch(event: E): Unit
}
