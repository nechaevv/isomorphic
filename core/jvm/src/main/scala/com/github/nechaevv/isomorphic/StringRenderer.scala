package com.github.nechaevv.isomorphic

object StringRenderer extends Renderer[String] {
  override def element(name: String, modifiers: ElementModifier*): String = {
    def parseModifiers(mods: Iterable[ElementModifier]): (Seq[String], Seq[String]) = modifiers.foldLeft(Seq.empty[String], Seq.empty[String])((acc, modifier) ⇒ {
      val (attributes, children) = acc
      modifier match {
        case Attribute(n, v) ⇒ (attributes :+ s""""$n"="$v"""", children)
        case ChildElement(e) ⇒ (attributes, children :+ e(this))
        case MultiModifier(mm) ⇒
          val (na, nc) = parseModifiers(mm)
          (attributes ++ na, children ++ nc)
      }
    })
    val (attributes, children) = parseModifiers(modifiers)
    s"<$name ${attributes.mkString(" ")}>${children.mkString("")}</$name>"
  }

  override def fragment(contents: String*): String = contents.mkString("")
  override def text(content: String): String = content
}
