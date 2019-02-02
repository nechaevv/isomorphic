package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic.dom._
import com.github.nechaevv.isomorphic.dom.browser._
import com.github.nechaevv.isomorphic.frontend.DOMEventTypes
import dsl._
import tags._
import org.scalajs.dom.Event

object TaskListComponent {
 def apply(): Component[Seq[Task], TagNode] = { tasks ⇒
   ul(
     (for ((task, index) ← tasks.zipWithIndex) yield {
       val taskListController = DOMEventTypes.Click → ((e: Event) ⇒ fs2.Stream(TaskSelectEvent(index)))
       li.withKey(index.toString).props(
         'class := (if(task.completed) "completed" else "uncompleted"),
         taskListController
       )(
         span.props('class := "number")(index.toString),
         " - ",
         span.props('class := "name")(task.name)
       )
     }):_*
   )
 }
}
