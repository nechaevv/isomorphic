package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic.Tags._
import com.github.nechaevv.isomorphic._

object AppComponent extends Component[TasksState] {
  override def apply(state: TasksState): Element = {
    div(
      new PimpedComponent[Seq[Task]](TaskListComponent) << state.tasks,
      new PimpedComponent[Task](TaskEditComponent) << state.editingTask,
      state.message.map(msg ⇒ p(msg))
    )
  }
}
