package com.github.nechaevv

package object core {
  type Component[E, S] = Renderer[E] ⇒ S ⇒ E
}
