package com.github.nechaevv.tasks

import com.github.nechaevv.core.dsl._
import com.github.nechaevv.core.{Component, Element}
import com.github.nechaevv.pipeline.EventDispatcher

object AppComponent extends Component[TasksState, AppEvent] {
  override def apply(state: TasksState, events: EventDispatcher[AppEvent]): Element = {
    div(
      TaskListComponent(state.tasks, events),
      TaskEditComponent(state.editingTask, events)
    )
  }
}
