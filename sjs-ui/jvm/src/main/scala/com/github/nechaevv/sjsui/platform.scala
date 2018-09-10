package com.github.nechaevv.sjsui

import com.github.nechaevv.sjsui.dom.DomEvent

object platform extends UiPlatform {
  override type Event = DomEvent
  override val functionCache: FunctionCache = new FunctionCache {
    override def memoize[A, B](func: A ⇒ B): A ⇒ B = func
  }
}
