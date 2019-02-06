package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic.frontend.DOMEventTypes
import com.github.nechaevv.isomorphic.vdom._
import com.github.nechaevv.isomorphic.vdom.browser._
import tags._

object AppComponent extends Component[TasksState, FragmentVNode] {
  override def apply(state: TasksState): FragmentVNode = fragment(
    section(classes += "header",
      h1("ToDo sample app"),
      state.editingIndex.map(i ⇒ p(
        "Selected item:",
        span(i.toString)
      )),
      h2("Current route: ", state.route)
    ),
    TaskListComponent() << state.tasks,
    TaskEditComponent() << state.editingTask,
    state.message.map(msg ⇒ p(classes += "message",
      "Message:",
      span(msg)
    )),
    a('href := "#", DOMEventTypes.Click → goDashboard, "Dashboard")
  )

  val goDashboard: EventHandler = e ⇒ {
    e.preventDefault()
    fs2.Stream(NavigateToDashboard)
  }
}
