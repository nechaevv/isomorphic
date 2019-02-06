package com.github.nechaevv.isomorphic.example

import monocle.macros.Lenses

@Lenses
case class TasksState
(
  route: String,
  tasks: Seq[Task],
  editingIndex: Option[Int],
  editingTask: Task,
  message: Option[String]
)
