package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic._
import com.github.nechaevv.isomorphic.platform._

object AppComponent extends Component[TasksState, AppEvent] {
  override def apply(state: TasksState, events: EventDispatcher[AppEvent]): Element = {
    div(
      TaskListComponent(state.tasks, events),
      TaskEditComponent(state.editingTask, events),
      state.message.map(msg ⇒ p(msg))
    )
  }
}
