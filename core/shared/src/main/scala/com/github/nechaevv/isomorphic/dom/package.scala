package com.github.nechaevv.isomorphic

package object dom {
  type Component[-S, +N <: Node] = S ⇒ N
}
