package com.github.nechaevv.core

import scala.scalajs.js
import scala.scalajs.js.Dynamic.global

class ComponentFactory[T](val renderer: Renderer[T]) {
  private val componentCache = js.Dynamic.newInstance(global.WeakMap)()

  def apply[S](render: (S, Renderer[T]) ⇒ T): Component[S, T] = { state: S ⇒
    val cachedValue = componentCache.get(state.asInstanceOf[js.Any])
    if (js.isUndefined(cachedValue)) {
      val newValue = render(state, renderer)
      componentCache.set(newValue.asInstanceOf[js.Any])
      newValue
    } else cachedValue.asInstanceOf[T]
  }
}
