package com.github.nechaevv

package object core {
  type Component[S, R <: Renderer] = S ⇒ R#Element
}
