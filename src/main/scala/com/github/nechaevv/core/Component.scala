package com.github.nechaevv.core

import com.github.nechaevv.pipeline.EventDispatcher

trait Component[S, E] {
  def apply(state: S, events: EventDispatcher[E]): Element
}
