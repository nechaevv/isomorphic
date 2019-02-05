package com.github.nechaevv.isomorphic

import scala.language.implicitConversions

package object vdom {

  type Component[-S, +N <: VNode] = S ⇒ N

  implicit class PimpedString(s: String) {
    def :=(value: String): VNodeAttribute = VNodeAttribute(s, value)
    def :=(value: Boolean): VNodeAttribute = VNodeAttribute(s, if (value) "true" else "false")
    def ?=(value: Boolean): VNodeModifier = if (value) VNodeAttribute(s, "") else EmptyModifier
  }

  implicit class PimpedSymbol(s: Symbol) extends PimpedString(s.name)

  implicit class PimpedComponent[S, N <: VNode](c: Component[S, N]) {
    def << (state: S): ComponentVNode[S, N] = ComponentVNode(c, state)
  }

  implicit def childToModifier[T](child: T)(implicit conv: T => VNode): VNodeChild = VNodeChild(conv(child))

  implicit def textToVNode(text: String): VNode = TextVNode(text)

  implicit def seqToVNode(seq: Seq[VNode]): FragmentVNode = FragmentVNode(seq)

  implicit def optionToVNode(o: Option[VNode]): FragmentVNode = FragmentVNode(o.toSeq)

  implicit def autonomousWebComponentToVNode(webComponent: AutonomousCustomElement): ElementVNode = ElementVNode(webComponent.elementName)

  implicit def extensionWebComponentToVNode(webComponent: ExtensionCustomElement): ElementVNode = ElementVNode(webComponent.extendedElement)
    .apply(VNodeAttribute("is", webComponent.elementName))

  def fragment(children: VNode*): FragmentVNode = FragmentVNode(children)

  object classes {
    def +=(name: String) = VNodeClass(name)
  }

}
