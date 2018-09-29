package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic._
import com.github.nechaevv.isomorphic.frontend._
import Tags._

import org.scalajs.dom.raw.HTMLInputElement

object TaskEditComponent extends Component[Task] {
  def apply(task: Task, dispatcher: EventDispatcher): Element = {
    val save: EventHandler = _ ⇒ dispatcher.dispatch(TaskSaveEvent)
    val nameChange: EventHandler = e ⇒ dispatcher.dispatch(TaskEditNameEvent(e.target.asInstanceOf[HTMLInputElement].value))
    val completedChange: EventHandler = e ⇒ dispatcher.dispatch(TaskSetCompletedEvent(e.target.asInstanceOf[HTMLInputElement].checked))
    form(
      input('type := "text", 'value := task.name, DOMEventTypes.Change → nameChange),
      input('type := "checkbox", 'checked := (if (task.completed) "checked" else "") , DOMEventTypes.Change → completedChange),
      button('type := "button", DOMEventTypes.Click → save, "SAVE")
    )
  }
}
