package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic.dom._
import com.github.nechaevv.isomorphic.frontend.DOMEventTypes
import dsl._
import tags._
import org.scalajs.dom.Event

object TaskListComponent {
 def apply(): Component[Seq[Task], TagNode] = { tasks ⇒
   ul(
     (for ((task, index) ← tasks.zipWithIndex) yield {
       val taskListController = DOMEventTypes.Click → ((e: Event) ⇒ fs2.Stream(TaskSelectEvent(index)))
       li.props('class := (if(task.completed) "completed" else "uncompleted"))(
         task.name
         //taskListController
       )
     }):_*
   )
 }
}
