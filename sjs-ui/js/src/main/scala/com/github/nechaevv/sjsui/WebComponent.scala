package com.github.nechaevv.sjsui

trait WebComponent {
  type Event
  type State
  type Component <: platform.Component[State, Event]

  def tagName: String
  def attributes: Iterable[String] = Seq.empty
  def rootComponent: Component
  def initialState: State
  def reducer: Reducer[Event, State]
  def useShadowRoot: Boolean = false

  def initEvent(properties: Iterable[(String, String)]): Event
  def connectedEffect: Option[Event] = None
  def disconnectedEffect: Option[Event] = None
  def adoptedEffect: Option[Event] = None
  def attributeChangedEffect(name: String, oldValue: String, newValue: String): Option[Event] = None

  import platform._
  def apply = new Tag(tagName)

}
