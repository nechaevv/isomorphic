package com.github.nechaevv.isomorphic.example

import cats.effect.IO
import com.github.nechaevv.isomorphic.dom._
import com.github.nechaevv.isomorphic.dom.browser._
import com.github.nechaevv.isomorphic.frontend.DOMEventTypes
import dsl._
import tags._
import org.scalajs.dom.Event

object TaskListComponent {
  case class TaskClickHandler(index: Int) extends EventHandler {
    override def apply(e: Event): fs2.Stream[IO, Any] = fs2.Stream(TaskSelectEvent(index))
  }
  case class TaskDeleteClickHandler(index: Int) extends EventHandler {
    override def apply(e: Event): fs2.Stream[IO, Any] = fs2.Stream(TaskDeleteEvent(index))
  }
  def apply(): Component[Seq[Task], TagNode] = { tasks ⇒
    ul(
      for ((task, index) ← tasks.zipWithIndex) yield {
        val taskClickController: NodeEventListener = DOMEventTypes.Click → TaskClickHandler(index)
        val taskDeleteClickController: NodeEventListener = DOMEventTypes.Click → TaskDeleteClickHandler(index)
        li.withKey(index.toString).attr(
          'class := (if (task.completed) "completed" else "uncompleted"),
        )(
          span.attr('class := "number")(index.toString),
          " - ",
          span.attr('class := "name", taskClickController)(task.name),
          " ",
          button.attr(taskDeleteClickController)("DEL")
        )
      }
    )
  }
}
