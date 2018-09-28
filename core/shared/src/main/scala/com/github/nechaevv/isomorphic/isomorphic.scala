package com.github.nechaevv

import cats.effect.IO
import fs2.Stream

import scala.language.implicitConversions

package object isomorphic {
  type Component[S] = (S, EventDispatcher) ⇒ Element
  type Reducer[S] = PartialFunction[Any, S ⇒ S]
  type Effect[S] = PartialFunction[Any, S ⇒ Stream[IO, Any]]

  implicit class PimpedSymbol(s: Symbol) {
    def :=(value: String) : Attribute = Attribute(s.name, value)
    def :?(value: Boolean) : MultiModifier = MultiModifier(if (value) Seq(Attribute(s.name, s.name)) else Seq.empty[Attribute])
  }

  implicit class PimpedString(s: String) {
    def :=(value: String) : Attribute = Attribute(s, value)
    def :?(value: Boolean) : MultiModifier = MultiModifier(if (value) Seq(Attribute(s, s)) else Seq.empty[Attribute])
  }

  implicit def childElementModifier(element: Element) : ChildElement = ChildElement(element)
  implicit def childTextModifier(text: String): ChildElement = ChildElement(new Element {
    override def apply[E](renderer: Renderer[E]): E = renderer.text(text)
  })
  implicit def modifierIterableImplicit[T](mods: Iterable[T])(implicit conv: T ⇒ ElementModifier): MultiModifier = MultiModifier(mods.map(conv))
  implicit def modifierOptionImplicit[T](mod: Option[T])(implicit conv: T ⇒ ElementModifier): MultiModifier = MultiModifier(mod.map(conv))

  implicit def autonomousWebComponentToTag(webComponent: AutonomousWebComponent): Tag = new Tag(webComponent.elementName)

  implicit def extensionWebComponentToTag(webComponent: ExtensionWebComponent): Tag = new Tag(webComponent.extendedElement) {
    override def apply(modifiers: ElementModifier*): Element = super.apply(modifiers :+ Attribute("is", webComponent.elementName))
  }
}
