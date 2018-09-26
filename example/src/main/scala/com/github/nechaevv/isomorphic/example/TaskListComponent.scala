package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic.platform._
import com.github.nechaevv.isomorphic._

object TaskListComponent extends Component[Seq[Task], AppEvent] {
 def apply(tasks: Seq[Task], dispatcher: EventDispatcher[AppEvent]): Element = {
   ul(
     for ((task, index) ← tasks.zipWithIndex) yield {
       val click: EventHandler = e ⇒ dispatcher.dispatch(TaskSelectEvent(index))
       li(
         'class := (if(task.completed) "completed" else "uncompleted"), EventTypes.Click → click, task.name
       )
     }
   )
 }
}
