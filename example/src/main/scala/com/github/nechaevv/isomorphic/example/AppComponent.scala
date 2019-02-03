package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic.dom._
import dsl._
import tags._

object AppComponent extends Component[TasksState, FragmentNode] {
  override def apply(state: TasksState): FragmentNode = fragment(
    section.attr('class := "header")(
      h1("ToDo sample app"),
      state.editingIndex.map(i ⇒ p(
        "Selected item:",
        span(i.toString)
      ))
    ),
    TaskListComponent() << state.tasks,
    TaskEditComponent() << state.editingTask,
    state.message.map(msg ⇒ p.attr('class := "message")(
      "Message:",
      span(msg)
    ))
  )
}
