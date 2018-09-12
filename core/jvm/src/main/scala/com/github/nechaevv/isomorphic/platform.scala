package com.github.nechaevv.isomorphic

import com.github.nechaevv.isomorphic.dom.DomEvent

object platform extends UiPlatform {
  override type Event = DomEvent
  override val functionCache: FunctionCache = new FunctionCache {
    override def memoize[A, B](func: A ⇒ B): A ⇒ B = func
  }
}
