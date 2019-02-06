package com.github.nechaevv.isomorphic.webcomponent

import cats.effect.{Concurrent, ContextShift, IO}
import com.github.nechaevv.isomorphic.EventDispatcher
import fs2.Stream
import fs2.concurrent.Queue
import org.scalajs.dom.raw.HTMLElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.control.NonFatal

class StatefulHostElementDelegate(statefulHostComponent: StatefulHostComponent,
                                  componentHost: HTMLElement) extends CustomElementDelegate {

  implicit val defaultContextShift: ContextShift[IO] = IO.contextShift(global)

  protected val eventDispatcher: EventDispatcher = Queue.unbounded[IO, Any].unsafeRunSync()

  protected def sendEvent(event: Any): Unit = eventDispatcher.enqueue1(event).unsafeRunSync()

  override def connectedCallback(): Unit = eventReducerPipeline().unsafeRunAsyncAndForget()

  override def disconnectedCallback(): Unit = sendEvent(ComponentDisconnectedEvent)

  override def adoptedCallback(): Unit = sendEvent(ComponentAdoptedEvent)

  override def attributeChangedCallback(name: String, oldValue: String, newValue: String): Unit = {
    sendEvent(ComponentAttributeChangedEvent(name, oldValue, newValue))
  }

  protected def eventReducerPipeline()(implicit concurrent: Concurrent[IO]): IO[Unit] = {
    val initState = statefulHostComponent.initialState(statefulHostComponent.attributes
      .map(attribute ⇒ attribute → componentHost.getAttribute(attribute)))
    val renderFn = statefulHostComponent.render(componentHost)(eventDispatcher)
    val events = eventDispatcher.dequeue.takeWhile(event ⇒ event != ComponentDisconnectedEvent, takeFailure = true)
    (for {
      reducerOutput ← events.scan[(statefulHostComponent.State, Any, Boolean)]((initState, ComponentConnectedEvent, true))((acc, event: Any) ⇒ {
        val (state, _, _) = acc
        if (event == ComponentConnectedEvent) acc
        else {
          val newState = try {
            statefulHostComponent.reducer(event)(state)
          } catch {
            case NonFatal(err) ⇒
              err.printStackTrace()
              state
          }
          (newState, event, !(state eq newState))
        }
      })
      (state, event, hasChanged) = reducerOutput
      renderStream = if (hasChanged) Stream.eval(IO {
        println(s"Tick, event: $event")
        renderFn(state)
      }) else Stream.empty
      effectStream = Stream.eval(concurrent.start(
        statefulHostComponent.effect(event)(state).through(eventDispatcher.enqueue).compile.drain
          .handleErrorWith(err ⇒ IO(err.printStackTrace()))
      ))
      _ ← renderStream ++ effectStream
    } yield ()).compile.drain
  }

}
