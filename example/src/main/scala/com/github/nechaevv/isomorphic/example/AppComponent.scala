package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic.vdom._
import tags._

object AppComponent extends Component[TasksState, FragmentVNode] {
  override def apply(state: TasksState): FragmentVNode = fragment(
    section(classes += "header",
      h1("ToDo sample app"),
      state.editingIndex.map(i ⇒ p(
        "Selected item:",
        span(i.toString)
      ))
    ),
    TaskListComponent() << state.tasks,
    TaskEditComponent() << state.editingTask,
    state.message.map(msg ⇒ p(classes += "message",
      "Message:",
      span(msg)
    ))
  )
}
