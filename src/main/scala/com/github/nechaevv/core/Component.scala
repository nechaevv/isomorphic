package com.github.nechaevv.core

trait Component[S] {
  def apply[E](state: S)(implicit renderer: Renderer[E]): E
}
