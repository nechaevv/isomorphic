package com.github.nechaevv

import cats.effect.IO
import fs2.Stream
import fs2.concurrent.Queue

import scala.language.implicitConversions

package object isomorphic {
  type Component[S] = S ⇒ Element
  type Reducer[S] = PartialFunction[Any, S ⇒ S]
  type Effect[S] = PartialFunction[Any, S ⇒ Stream[IO, Any]]
  type EventStream = Queue[IO, Any]

  implicit class PimpedString(s: String) {
    def :=(value: String) : Attribute = Attribute(s, value)
    def :=(value: Boolean) : Attribute = Attribute(s, if (value) "true" else "false")
    def :?(value: Boolean) : MultiModifier = MultiModifier(if (value) Seq(Attribute(s, s)) else Seq.empty[Attribute])
  }

  implicit class PimpedSymbol(s: Symbol) extends PimpedString(s.name)

  implicit def childElementModifier(element: Element) : ChildElement = ChildElement(element)
  implicit def childTextModifier(text: String): ChildElement = ChildElement(new Element {
    override def apply[E](renderer: Renderer[E]): E = renderer.text(text)
  })
  implicit def modifierIterableImplicit[T](mods: Iterable[T])(implicit conv: T ⇒ ElementModifier): MultiModifier = MultiModifier(mods.map(conv))
  implicit def modifierOptionImplicit[T](mod: Option[T])(implicit conv: T ⇒ ElementModifier): MultiModifier = MultiModifier(mod.map(conv))

  object classes {
    def +=(className: String): WithClass = WithClass(className)
    def ++=(classNames: Iterable[String]): MultiModifier = MultiModifier(classNames.map(WithClass))
  }

  implicit def autonomousWebComponentToTag(webComponent: AutonomousCustomElement): Tag = new Tag(webComponent.elementName)

  implicit def extensionWebComponentToTag(webComponent: ExtensionCustomElement): Tag = new Tag(webComponent.extendedElement)
    .append(Attribute("is", webComponent.elementName))

}
