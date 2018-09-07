package com.github.nechaevv.core

trait Element {
  def apply[E](renderer: Renderer[E]): E
}
