package com.github.nechaevv.isomorphic.example.components

import com.github.nechaevv.isomorphic.example.ClearMessages
import com.github.nechaevv.isomorphic.frontend.DOMEventTypes
import com.github.nechaevv.isomorphic.vdom._
import com.github.nechaevv.isomorphic.vdom.browser._
import com.github.nechaevv.isomorphic.vdom.tags._

object MessagesComponent extends Component[Seq[String], FragmentVNode] {
  override def apply(messages: Seq[String]): FragmentVNode = if (messages.nonEmpty) Some(div(
    h2("Messages"),
    button(classes += "clear", DOMEventTypes.Click → clearEventHandler, "clear"),
    for (msg ← messages) yield div(msg)
  )) else None
  val clearEventHandler: EventHandler = _ ⇒ fs2.Stream(ClearMessages)
}
