package com.github.nechaevv.isomorphic

object StringRenderer extends Renderer[String] {
  override def element(name: String, modifiers: ElementModifier*): String = {
    def parseModifiers(mods: Iterable[ElementModifier]): (Seq[String], Seq[String], Seq[String]) = modifiers
      .foldLeft(Seq.empty[String], Seq.empty[String], Seq.empty[String])((acc, modifier) ⇒ {
      val (attributes, children, classes) = acc
      modifier match {
        case Attribute(n, v) ⇒ (attributes :+ s""""${n.toLowerCase}"="$v"""", children, classes)
        case ChildElement(e) ⇒ (attributes, children :+ e(this), classes)
        case ChildComponent(c, s) ⇒ (attributes, children :+ c(s)(this), classes)
        case WithClass(className) ⇒ (attributes, children, classes :+ className)
        case MultiModifier(mm) ⇒
          val (na, nc, ncc) = parseModifiers(mm)
          (attributes ++ na, children ++ nc, classes ++ ncc)
      }
    })
    val (attributes, children, classes) = parseModifiers(modifiers)
    val classAttribute = if (classes.isEmpty) None else Some("class=\"" + classes.mkString(" ") + "\"")
    s"<$name ${(attributes ++ classAttribute).mkString(" ")}>${children.mkString("")}</$name>"
  }

  override def fragment(contents: String*): String = contents.mkString("")
  override def text(content: String): String = content
}
