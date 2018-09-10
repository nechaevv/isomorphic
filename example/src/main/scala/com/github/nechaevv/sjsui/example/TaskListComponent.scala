package com.github.nechaevv.sjsui.example

import com.github.nechaevv.sjsui.platform._
import com.github.nechaevv.sjsui._

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
