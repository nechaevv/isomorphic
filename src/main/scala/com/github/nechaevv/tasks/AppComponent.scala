package com.github.nechaevv.tasks

import com.github.nechaevv.core.{Component, Renderer}
import com.github.nechaevv.core.Tags._

object AppComponent {
  def apply[E](): Component[E, TasksState] = { renderer: Renderer[E] ⇒ state: TasksState ⇒
    div(renderer)(Map.empty, Seq(
      TaskListComponent()(renderer)(state.tasks),
      TaskEditComponent()(renderer)(state.editingTask)
    ))
  }
}
