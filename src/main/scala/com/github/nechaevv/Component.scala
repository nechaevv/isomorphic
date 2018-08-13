package com.github.nechaevv

trait Component[S] {
  def render[T](state: S)(implicit renderer: Renderer[T]): T
}