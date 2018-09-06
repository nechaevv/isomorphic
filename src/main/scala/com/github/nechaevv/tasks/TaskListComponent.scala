package com.github.nechaevv.tasks

import com.github.nechaevv.core.{Component, Renderer}
import com.github.nechaevv.core.Tags._

object TaskListComponent extends Component[Seq[Task]] {
 def apply[E](tasks: Seq[Task])(implicit renderer: Renderer[E]): E = {
   ul()(
     tasks.map(task ⇒ li(Map("class" → (if(task.completed) "completed" else "uncompleted")))(renderer.text(task.name))):_*
   )
 }
}
