package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic._
import com.github.nechaevv.isomorphic.frontend._
import Tags._
import org.scalajs.dom.Event
import org.scalajs.dom.raw.HTMLInputElement

object TaskEditComponent {
  def apply(): Component[Task] = { task ⇒
    val nameController = DOMEventTypes.Change → ((e: Event) ⇒ fs2.Stream(TaskEditNameEvent(e.target.asInstanceOf[HTMLInputElement].value)))
    val completedController = DOMEventTypes.Change → ((e: Event) ⇒ fs2.Stream(TaskSetCompletedEvent(e.target.asInstanceOf[HTMLInputElement].checked)))
    val saveController = DOMEventTypes.Click → ((e: Event) ⇒ fs2.Stream(TaskSaveEvent))
    form(
      input('type := "text", 'value := task.name, nameController),
      input('type := "checkbox", 'checked := (if (task.completed) "checked" else ""), completedController),
      button('type := "button", "SAVE", saveController)
    )
  }
}
