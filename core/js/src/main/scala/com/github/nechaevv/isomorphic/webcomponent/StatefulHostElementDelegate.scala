package com.github.nechaevv.isomorphic.webcomponent

import cats.effect.{Concurrent, ContextShift, IO}
import com.github.nechaevv.isomorphic.ActionDispatcher
import fs2.Stream
import fs2.concurrent.Queue
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.control.NonFatal

class StatefulHostElementDelegate(protected val statefulHostComponent: StatefulHostComponent,
                                  protected val componentHost: HTMLElement) extends CustomElementDelegate {

  implicit val defaultContextShift: ContextShift[IO] = IO.contextShift(global)

  protected val actionDispatcher: ActionDispatcher = Queue.unbounded[IO, Any].unsafeRunSync()

  protected def sendEvent(event: Any): Unit = actionDispatcher.enqueue1(event).unsafeRunSync()

  override def connectedCallback(): Unit = eventReducerPipeline().unsafeRunAsyncAndForget()

  override def disconnectedCallback(): Unit = sendEvent(ComponentDisconnectedEvent)

  override def adoptedCallback(): Unit = sendEvent(ComponentAdoptedEvent)

  override def attributeChangedCallback(name: String, oldValue: String, newValue: String): Unit = {
    sendEvent(ComponentAttributeChangedEvent(name, oldValue, newValue))
  }

  protected def eventReducerPipeline()(implicit concurrent: Concurrent[IO]): IO[Unit] = {
    val initState = statefulHostComponent.initialState(statefulHostComponent.attributes
      .map(attribute ⇒ attribute → componentHost.getAttribute(attribute)))
    val renderFn = statefulHostComponent.render(componentHost)(actionDispatcher)
    val events = actionDispatcher.dequeue.takeWhile(event ⇒ event != ComponentDisconnectedEvent, takeFailure = true)
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
        if (scheduledRenderState.isEmpty) dom.window.requestAnimationFrame(_ ⇒ scheduledRenderState.foreach(s ⇒ {
          renderFn(s)
          scheduledRenderState = None
        }))
        scheduledRenderState = Some(state)
      }) else Stream.empty
      effectStream = Stream.eval(concurrent.start(
        statefulHostComponent.effect(event)(state).through(actionDispatcher.enqueue).compile.drain
          .handleErrorWith(err ⇒ IO(err.printStackTrace()))
      ))
      _ ← renderStream ++ effectStream
    } yield ()).compile.drain
  }

  protected var scheduledRenderState: Option[statefulHostComponent.State] = None

}
