package com.github.nechaevv.core

import scala.scalajs.js
import scala.scalajs.js.Dynamic.global

object CachingComponent {
  private val componentCache = js.Dynamic.newInstance(global.WeakMap)()

  def apply[S, R <: Renderer](component: Component[S, R]): Component[S, R] = { state: S ⇒ renderer: R ⇒
    val cachedValue = componentCache.get(state.asInstanceOf[js.Any])
    if (js.isUndefined(cachedValue)) {
      val newValue = component(state)(renderer)
      componentCache.set(newValue.asInstanceOf[js.Any])
      newValue
    } else cachedValue.asInstanceOf[R#Element]
  }

}
