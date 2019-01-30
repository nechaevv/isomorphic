package com.github.nechaevv.isomorphic.example

import cats.Id
import cats.effect.IO
import com.github.nechaevv.isomorphic.{CustomElements, EventDispatcher}
import com.github.nechaevv.isomorphic.dom.{DomReconciler, DomReconciliationContext}
import fs2.{Chunk, Pipe}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object Main {
  @JSExportTopLevel("boot")
  def boot(): Unit = {
    //CustomElements.define(TasksApp, js.constructorOf[TasksAppStatefulHostCustomElement])
    val container = org.scalajs.dom.document.body
    val ed: EventDispatcher = new EventDispatcher {
      override def enqueue1(a: Any): IO[Unit] = ???

      override def offer1(a: Any): IO[Boolean] = ???

      override def dequeueChunk(maxSize: Int): fs2.Stream[IO, Any] = ???

      override def dequeueBatch: Pipe[IO, Int, Any] = ???

      override def dequeue1: IO[Any] = ???

      override def tryDequeue1: IO[Option[Any]] = ???

      override def dequeueChunk1(maxSize: Int): IO[Id[Chunk[Any]]] = ???

      override def tryDequeueChunk1(maxSize: Int): IO[Option[Id[Chunk[Any]]]] = ???
    }
    DomReconciler(StaticTestComponent(), DomReconciliationContext(container, ed, Seq.empty))
  }
}