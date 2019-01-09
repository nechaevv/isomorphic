package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic._
import com.github.nechaevv.isomorphic.frontend._
import Tags._
import org.scalajs.dom.Event

object TaskListComponent {
 def apply(): Component[Seq[Task]] = { tasks ⇒
   ul(
     for ((task, index) ← tasks.zipWithIndex) yield {
       val taskListController = DOMEventTypes.Click → ((e: Event) ⇒ fs2.Stream(TaskSelectEvent(index)))
       li(
         classes += (if(task.completed) "completed" else "uncompleted"),
         task.name,
         taskListController
       )
     }
   )
 }
}
