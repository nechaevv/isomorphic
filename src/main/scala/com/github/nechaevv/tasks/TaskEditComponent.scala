package com.github.nechaevv.tasks

import com.github.nechaevv.core.dsl._
import com.github.nechaevv.core.{Component, Element, EventTypes}
import com.github.nechaevv.pipeline.EventDispatcher
import org.scalajs.dom.raw.HTMLInputElement

object TaskEditComponent extends Component[Task, AppEvent] {
  def apply(task: Task, dispatcher: EventDispatcher[AppEvent]): Element = {
    val save: EventHandler = e ⇒ dispatcher.dispatch(TaskSaveEvent)
    val nameChange: EventHandler = e ⇒ dispatcher.dispatch(TaskEditNameEvent(e.currentTarget.asInstanceOf[HTMLInputElement].value))
    val completedChange: EventHandler = e ⇒ dispatcher.dispatch(TaskSetCompletedEvent(e.currentTarget.asInstanceOf[HTMLInputElement].checked))
    form(
      input("type" := "text", "value" := task.name, EventTypes.Change → nameChange),
      input("type" := "checkbox", "checked" :? task.completed, EventTypes.Change → completedChange),
      button("type" := "button", EventTypes.Click → save, "SAVE")
    )
  }
}
