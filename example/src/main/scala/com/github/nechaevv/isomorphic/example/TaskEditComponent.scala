package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic.dom._
import com.github.nechaevv.isomorphic.dom.browser._
import com.github.nechaevv.isomorphic.frontend.DOMEventTypes
import org.scalajs.dom.Event
import org.scalajs.dom.raw.HTMLInputElement
import tags._

object TaskEditComponent {
  private val nameController: VNodeEventListener = DOMEventTypes.Change → ((e: Event) ⇒ fs2.Stream(TaskEditNameEvent(e.target.asInstanceOf[HTMLInputElement].value)))
  private val completedController: VNodeEventListener = DOMEventTypes.Change → ((e: Event) ⇒ fs2.Stream(TaskSetCompletedEvent(e.target.asInstanceOf[HTMLInputElement].checked)))
  private val saveController: VNodeEventListener = DOMEventTypes.Click → ((e: Event) ⇒ fs2.Stream(TaskSaveEvent))

  def apply(): Component[Task, ElementVNode] = { task ⇒
    form(
      input('type := "text", 'value := task.name, nameController),
      input('type := "checkbox", 'checked ?= task.completed, completedController),
      button('type := "button", saveController, "SAVE")
    )
  }
}
