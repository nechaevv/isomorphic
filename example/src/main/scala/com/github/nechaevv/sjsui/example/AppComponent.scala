package com.github.nechaevv.sjsui.example

import com.github.nechaevv.sjsui.{EventDispatcher, _}
import com.github.nechaevv.sjsui.platform._

object AppComponent extends Component[TasksState, AppEvent] {
  override def apply(state: TasksState, events: EventDispatcher[AppEvent]): Element = {
    div(
      TaskListComponent(state.tasks, events),
      TaskEditComponent(state.editingTask, events)
    )
  }
}
