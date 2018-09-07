package com.github.nechaevv.core

trait Component[S] {
  def apply(state: S): Element
}
