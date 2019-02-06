package com.github.nechaevv.isomorphic.router

import com.github.nechaevv.isomorphic.webcomponent.StatefulHostElementDelegate

trait RouterSupport { this: StatefulHostElementDelegate ⇒

  override def connectedCallback(): Unit = this.connectedCallback()

  override def disconnectedCallback(): Unit = this.disconnectedCallback()
}
