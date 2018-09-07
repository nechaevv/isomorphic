package com.github.nechaevv.tasks

import com.github.nechaevv.core.Dsl._
import com.github.nechaevv.core.{Component, Element, EventTypes}
import org.scalajs.dom.Event

object TaskEditComponent extends Component[Task] {
  def apply(task: Task): Element = {
    val onClick: Event ⇒ Unit = e ⇒ System.out.println("clicked! " + e.target.toString)
    form(
      input("type" := "text", "value" := task.name),
      input("type" := "checkbox"), // if (task.completed) Some("checked" := "true") else None),
      button("type" := "button", EventTypes.Click → onClick, "SAVE")
    )
  }
}
