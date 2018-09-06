package com.github.nechaevv.tasks

import com.github.nechaevv.core.{Component, Renderer}
import com.github.nechaevv.core.Tags._
import org.scalajs.dom.Event

object TaskEditComponent extends Component[Task] {
  def apply[E](task: Task)(implicit renderer: Renderer[E]): E = {
    val onClick: Event ⇒ Unit = e ⇒ System.out.println("clicked! " + e.target.toString)
    form()(
      input(Map("type"→"text", "value"→task.name))(),
      input(Map("type"→"checkbox") ++ (if (task.completed) Seq("checked"→"true") else Seq.empty[(String,String)]))(),
      button(Map("type"→"button"), Map("onClick" → onClick))(renderer.text("SAVE"))
    )
  }
}
