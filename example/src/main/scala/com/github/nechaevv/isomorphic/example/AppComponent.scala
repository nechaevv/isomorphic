package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic._
import com.github.nechaevv.isomorphic.frontend._
import Tags._

object AppComponent extends Component[TasksState] {
  override def apply(state: TasksState, events: EventDispatcher): Element = {
    div(
      TaskListComponent(state.tasks, events),
      TaskEditComponent(state.editingTask, events),
      state.message.map(msg ⇒ p(msg))
    )
  }
}
