package com.github.nechaevv.tasks

import com.github.nechaevv.core.dsl._
import com.github.nechaevv.core.{Component, Element, EventTypes}
import com.github.nechaevv.pipeline.EventDispatcher

object TaskListComponent extends Component[Seq[Task], AppEvent] {
 def apply(tasks: Seq[Task], dispatcher: EventDispatcher[AppEvent]): Element = {
   ul(
     for ((task, index) ← tasks.zipWithIndex) yield {
       val click: EventHandler = e ⇒ dispatcher.dispatch(TaskSelectEvent(index))
       li(
         "class" := (if(task.completed) "completed" else "uncompleted"), EventTypes.Click → click, task.name
       )
     }
   )
 }
}
