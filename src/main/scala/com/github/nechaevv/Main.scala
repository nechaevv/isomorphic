package com.github.nechaevv

import com.github.nechaevv.dom.DomRenderer
import com.github.nechaevv.react.{ReactDOM, ReactRenderer}
import org.scalajs.dom.document
import com.github.nechaevv.tasks.{AppComponent, Task, TasksState}

object Main {
  def main(args: Array[String]): Unit = {
    val state = TasksState(Seq(Task("Task 1", true), Task("Task 2", false)), Task("Task name", false))
    val reactComponent = AppComponent()(ReactRenderer)
    val reactContainer = document.getElementById("ReactContainer")
    ReactDOM.render(reactComponent(state), reactContainer)

    val domComponent = AppComponent()(DomRenderer)
    val domContainer = document.getElementById("DomContainer")
    domContainer.appendChild(domComponent(state))

  }
}
