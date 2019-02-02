package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic.dom._
import dsl._
import tags._

object AppComponent extends Component[TasksState, FragmentNode] {
  override def apply(state: TasksState): FragmentNode = fragment(
    section.props('class := "header")(
      h1("ToDo sample app"),
      div("Selected item:", span(state.editingIndex.toString))
    ),
    TaskListComponent() << state.tasks,
    TaskEditComponent() << state.editingTask,
    div.props('class := "message")(
      "Message:",
      span(state.message.toString)
    )
//    state.message.map(msg ⇒ p(msg))
  )
}
