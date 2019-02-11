package com.github.nechaevv.isomorphic.router

import com.github.nechaevv.isomorphic.webcomponent.StatefulHostElementDelegate
import org.scalajs.dom
import org.scalajs.dom.Event

trait RouterSupport extends StatefulHostElementDelegate {

  val popStateListener: Event ⇒ Unit = event ⇒ this.sendEvent(
    RouteChangeEvent(Router.parseLocation(dom.window.location))
  )

  abstract override def connectedCallback(): Unit = {
    super.connectedCallback()
    dom.window.addEventListener("popstate", popStateListener)
  }

  abstract override def disconnectedCallback(): Unit = {
    dom.window.removeEventListener("popstate", popStateListener)
    super.disconnectedCallback()
  }
}
