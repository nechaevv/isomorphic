package com.github.nechaevv.tasks

import com.github.nechaevv.core.{Component, Renderer}
import com.github.nechaevv.core.Tags._

object AppComponent extends Component[TasksState] {

  override def apply[E](state: TasksState)(implicit renderer: Renderer[E]): E = {
    div()(
      TaskListComponent(state.tasks),
      TaskEditComponent(state.editingTask)
    )
  }
}
