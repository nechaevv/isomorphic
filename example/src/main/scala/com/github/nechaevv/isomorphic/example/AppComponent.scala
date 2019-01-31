package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic.dom._
import dsl._
import tags._

object AppComponent extends Component[TasksState] {
  val appTasks = TagNode("app-tasks")
  override def apply(state: TasksState): Node = {
    appTasks(
      h1("ToDo sample app"),
      TaskListComponent() << state.tasks,
//      TaskEditComponent() << state.editingTask,
//      state.message.map(msg ⇒ p(msg))
    )
  }
}
