package com.github.nechaevv.isomorphic.example

import cats.effect.IO
import com.github.nechaevv.isomorphic.dom._
import com.github.nechaevv.isomorphic.dom.browser._
import com.github.nechaevv.isomorphic.frontend.DOMEventTypes
import tags._
import org.scalajs.dom.Event

object TaskListComponent {
  case class TaskClickHandler(index: Int) extends EventHandler {
    override def apply(e: Event): fs2.Stream[IO, Any] = fs2.Stream(TaskSelectEvent(index))
  }
  case class TaskDeleteClickHandler(index: Int) extends EventHandler {
    override def apply(e: Event): fs2.Stream[IO, Any] = fs2.Stream(TaskDeleteEvent(index))
  }
  def apply(): Component[Seq[Task], ElementVNode] = { tasks ⇒
    ul(
      for ((task, index) ← tasks.zipWithIndex) yield {
        val taskClickController: VNodeEventListener = DOMEventTypes.Click → TaskClickHandler(index)
        val taskDeleteClickController: VNodeEventListener = DOMEventTypes.Click → TaskDeleteClickHandler(index)
        li.withKey(index.toString)(
          'class := (if (task.completed) "completed" else "uncompleted"),
          span('class := "number", index.toString),
          " - ",
          span('class := "name", taskClickController, task.name),
          " ",
          button(taskDeleteClickController, "DEL")
        )
      }
    )
  }
}
