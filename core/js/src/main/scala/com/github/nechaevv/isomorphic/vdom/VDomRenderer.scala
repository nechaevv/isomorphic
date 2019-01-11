package com.github.nechaevv.isomorphic.vdom

import com.github.nechaevv.isomorphic.{ElementModifier, FunctionCache, Renderer}

class VDomRenderer(componentCache: FunctionCache) extends Renderer[VNode] {
  override def element(name: String, modifiers: ElementModifier*): VNode = ???

  override def fragment(contents: VNode*): VNode = ???

  override def text(content: String): VNode = VText(content)
}
