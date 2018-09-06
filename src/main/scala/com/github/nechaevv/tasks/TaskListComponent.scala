package com.github.nechaevv.tasks

import com.github.nechaevv.core.{Component, Renderer}
import com.github.nechaevv.core.Tags._

object TaskListComponent {
 def apply[E](): Component[E, Seq[Task]] = { renderer: Renderer[E] ⇒ { tasks: Seq[Task] ⇒
   ul(renderer)(Map.empty, tasks.map(task ⇒ li(renderer)(Map("class" → (if(task.completed) "completed" else "uncompleted")), Seq(renderer.text(task.name)))))
 } }
}
