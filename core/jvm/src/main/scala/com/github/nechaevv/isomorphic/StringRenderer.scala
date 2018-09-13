package com.github.nechaevv.isomorphic

abstract class StringRenderer extends platform.Renderer[String] {
  override def element(name: String, attributes: Iterable[(String, String)], eventListeners: Iterable[(EventType, platform.Event ⇒ Unit)], childElements: Seq[String]): String = {
    val attributesString = attributes.map({
      case (n, v) ⇒ s""""$n"="$v""""
    }).mkString(" ")
    s"<$name $attributesString>${childElements.mkString("")}</$name>"
  }

  override def fragment(contents: String*): String = contents.mkString("")
  override def text(content: String): String = content
}
