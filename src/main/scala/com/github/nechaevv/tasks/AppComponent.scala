package com.github.nechaevv.tasks

import com.github.nechaevv.core.{Component, Element, Renderer}
import com.github.nechaevv.core.Dsl._

object AppComponent extends Component[TasksState] {
  override def apply(state: TasksState): Element = {
    div(
      TaskListComponent(state.tasks),
      TaskEditComponent(state.editingTask)
    )
  }
}
