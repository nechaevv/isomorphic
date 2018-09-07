package com.github.nechaevv.tasks

import com.github.nechaevv.core.{Component, Element, Renderer}
import com.github.nechaevv.core.dsl._

object TaskListComponent extends Component[Seq[Task]] {
 def apply(tasks: Seq[Task]): Element = {
   ul(
     for (task ← tasks) yield li(
       "class" := (if(task.completed) "completed" else "uncompleted"), task.name
     )
   )
 }
}
