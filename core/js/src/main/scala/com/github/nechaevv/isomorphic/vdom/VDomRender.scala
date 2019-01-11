package com.github.nechaevv.isomorphic.vdom

import com.github.nechaevv.isomorphic.{Element, EventDispatcher, StatefulHostComponent}
import org.scalajs.dom.raw.HTMLElement

class VDomRender extends { this: StatefulHostComponent ⇒
  override def render(componentHost: HTMLElement): (Element, EventDispatcher) ⇒ Unit = ???
}
