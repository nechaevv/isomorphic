package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic._
import com.github.nechaevv.isomorphic.frontend._
import Tags._
import org.scalajs.dom.Event
import org.scalajs.dom.raw.HTMLInputElement

object TaskEditComponent extends Component[Task] {
  def apply(task: Task, dispatcher: EventDispatcher): Element = {
    def nameChange(e: Event) = TaskEditNameEvent(e.target.asInstanceOf[HTMLInputElement].value)
    def completedChange(e: Event) = TaskSetCompletedEvent(e.target.asInstanceOf[HTMLInputElement].checked)
    form(
      input('type := "text", 'value := task.name, DOMEventTypes.Change → nameChange),
      input('type := "checkbox", 'checked := (if (task.completed) "checked" else "") , DOMEventTypes.Change → completedChange),
      button('type := "button", DOMEventTypes.Click → TaskSaveEvent, "SAVE")
    )
  }
}
