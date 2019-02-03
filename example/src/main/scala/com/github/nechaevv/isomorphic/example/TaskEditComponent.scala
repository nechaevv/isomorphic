package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic.dom._
import com.github.nechaevv.isomorphic.dom.browser._
import com.github.nechaevv.isomorphic.frontend.DOMEventTypes
import dsl._
import tags._
import org.scalajs.dom.Event
import org.scalajs.dom.raw.HTMLInputElement

object TaskEditComponent {
  private val nameController: NodeEventListener = DOMEventTypes.Change → ((e: Event) ⇒ fs2.Stream(TaskEditNameEvent(e.target.asInstanceOf[HTMLInputElement].value)))
  private val completedController: NodeEventListener = DOMEventTypes.Change → ((e: Event) ⇒ fs2.Stream(TaskSetCompletedEvent(e.target.asInstanceOf[HTMLInputElement].checked)))
  private val saveController: NodeEventListener = DOMEventTypes.Click → ((e: Event) ⇒ fs2.Stream(TaskSaveEvent))

  def apply(): Component[Task, TagNode] = { task ⇒
    form(
      input.props('type := "text", 'value := task.name, nameController),
      input.props('type := "checkbox", 'checked := (if (task.completed) "checked" else ""), completedController),
      button.props('type := "button", saveController)("SAVE")
    )
  }
}
