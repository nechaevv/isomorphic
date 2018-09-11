package com.github.nechaevv.sjsui

import cats.effect.internals.IOContextShift
import cats.effect.{ContextShift, IO}
import com.github.nechaevv.sjsui.react.{ReactDOM, ReactRenderer}
import fs2.Stream
import fs2.concurrent.Queue
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js

abstract class WebComponent extends HTMLElement {
  super()

  type Event
  type State
  type Component <: platform.Component[State, Event]

  def rootComponent: Component
  def initialState: State
  def reducer: Reducer[Event, State]

  def initEvent(element: HTMLElement): Event
  def connectedEffect: Option[Event] = None
  def disconnectedEffect: Option[Event] = None
  def adoptedEffect: Option[Event] = None
  def attributeChangedEffect(name: String, oldValue: String, newValue: String): Option[Event] = None

  implicit val defaultContextShift: ContextShift[IO] = IOContextShift.global

  private var dispatcher: Option[EventDispatcher[Event]] = None

  def connectedCallback(): Unit = for {
    event ← connectedEffect
    dispatcher ← dispatcher
  } dispatcher.dispatch(event)

  def disconnectedCallback(): Unit = for {
    event ← disconnectedEffect
    dispatcher ← dispatcher
  } dispatcher.dispatch(event)

  def adoptedCallback(): Unit = for {
    event ← adoptedEffect
    dispatcher ← dispatcher
  } dispatcher.dispatch(event)

  def attributeChangedCallback(name: String, oldValue: String, newValue: String): Unit = for {
    event ← attributeChangedEffect(name, oldValue, newValue)
    dispatcher ← dispatcher
  } dispatcher.dispatch(event)

  {
    val stream = for {
      eventStream ← Stream.eval(Queue.unbounded[IO, Event])
      _ ← Stream.eval(eventStream.enqueue1(initEvent(this)))
      eventDispatcher = {
        val dispatcherImpl = new QueueEventDispatcher[Event](eventStream)
        dispatcher = Some(dispatcherImpl)
        dispatcherImpl
      }
      events = eventStream.dequeue
      state ← events.scan(initialState)((state, event) ⇒ {
        val eventReducer: State ⇒ State = if (reducer.isDefinedAt(event)) reducer(event) else s ⇒ s
        eventReducer(state)
      })
      _ ← Stream.eval(IO {
        val reactComponent = rootComponent(state, eventDispatcher)(ReactRenderer)
        ReactDOM.render(reactComponent, this)
      } )
    } yield ()
    stream.compile.drain
  }

}
