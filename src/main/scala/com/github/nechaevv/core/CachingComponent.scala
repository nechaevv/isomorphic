package com.github.nechaevv.core

import scala.scalajs.js
import scala.scalajs.js.Dynamic.global

object CachingComponent {
  private val componentCache = js.Dynamic.newInstance(global.WeakMap)()

  def apply[E, S](component: Component[E, S]): Component[E, S] = { renderer: Renderer[E] ⇒ state: S ⇒
    val cachedValue = componentCache.get(state.asInstanceOf[js.Any])
    if (js.isUndefined(cachedValue)) {
      val newValue = component(renderer)(state)
      componentCache.set(newValue.asInstanceOf[js.Any])
      newValue
    } else cachedValue.asInstanceOf[E]
  }

}
