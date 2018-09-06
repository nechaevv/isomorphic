package com.github.nechaevv.tasks

import com.github.nechaevv.core.{Component, Renderer}
import com.github.nechaevv.core.Tags._

object TaskEditComponent {
  def apply[E](): Component[E, Task] = { renderer: Renderer[E] ⇒ { task: Task ⇒
    form(renderer)(Map.empty, Seq(
      input(renderer)(Map("type"→"text", "value"→task.name), Nil),
      input(renderer)(Map("type"→"checkbox") ++ (if (task.completed) Seq("checked"→"true") else Seq.empty[(String,String)]), Nil),
      input(renderer)(Map("type"→"submit"), Nil)
    ))
  } }
}
