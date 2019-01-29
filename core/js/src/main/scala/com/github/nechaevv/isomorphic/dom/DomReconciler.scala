package com.github.nechaevv.isomorphic.dom

import com.github.nechaevv.isomorphic.EventDispatcher
import com.github.nechaevv.isomorphic.api.WeakMap
import org.scalajs.dom.document
import org.scalajs.dom.raw

import scala.scalajs.js

object DomReconciler {

  private val componentCache = new WeakMap[ComponentNode[_], Node]
  private val nodeReprCache = new WeakMap[Node, NodeRepr]
  private val elementMapping = new WeakMap[NodeRepr, org.scalajs.dom.Node]

  trait NodeRepr {
    def key: String
  }

  case class TagRepr(name: String, attrs: Map[String, String], eventListeners: Set[NodeEventListener],
                     children: Seq[NodeRepr], key: String) extends NodeRepr
  case class FragmentRepr(children: Seq[NodeRepr], key: String) extends NodeRepr
  case class TextRepr(text:String, key: String) extends NodeRepr

  def apply(vdom: Seq[Node], context: DomReconciliationContext): DomReconciliationContext = {
    reconcileNodeSeq(context.currentVDom, vdom, context.container, context.eventDispatcher)
    context.copy(currentVDom = vdom)
  }

  private def reconcileNodeSeq(currentVDom: Seq[Node], vdom: Seq[Node], container: raw.HTMLElement,
                               eventDispatcher: EventDispatcher): Unit = {
    val currentVdomRepr = currentVDom.map(nodeReprCache.get(_).toOption).zipWithIndex
    var recycledElements = List.empty[raw.Node]
    var auxId = 0
    val vdomRepr = for ((node, nodeIndex) ← vdom.zipWithIndex) yield {

      var key = node.key.getOrElse({ auxId += 1; "$" + auxId.toString})
      var matchingNodeOpt = currentVdomRepr.find(el ⇒ el._1.exists(_.key == key)).map({
        case (Some(el), elIndex) ⇒ el → elIndex
      })
      var replaceNode = matchingNodeOpt.isEmpty || matchingNodeOpt.exists(_._2 != nodeIndex)

      node match {
        case TagNode(name, props, children, _) ⇒
          val (node, nodeRepr) = (for {
            (tagNodeRepr: TagRepr, tagNodeIndex) ← matchingNodeOpt
            domNode ← elementMapping.get(tagNodeRepr).toOption
              if domNode.isInstanceOf[raw.Element] && domNode.asInstanceOf[raw.Element].tagName == name
          } yield {

          })

        case FragmentNode(children, _) ⇒

        case cn: ComponentNode[_] ⇒

        case tn @ TextNode(text) ⇒
          val (node, nodeRepr) = (for {
            (textNodeRepr: TextRepr, textNodeIndex) ← matchingNodeOpt
            domNode ← elementMapping.get(textNodeRepr).toOption if domNode.isInstanceOf[raw.Text]
          } yield {
            if (textNodeRepr.text != text) {
              domNode.asInstanceOf[raw.Text].data = text
              val repr = TextRepr(text, key)
              elementMapping.set(repr, domNode)
              (domNode, repr)
            } else (domNode, textNodeRepr)
          }).getOrElse((document.createTextNode(text), TextRepr(text, key)))
          elementMapping.set(nodeRepr, node)
          nodeReprCache.set(tn, nodeRepr)
          if (replaceNode) {
            container.appendChild(node)
          }
          recycledElements = node :: recycledElements
          nodeRepr
      }
    }
  }

  /*
  private def buildRepr(vdom: Seq[Node]): Seq[NodeRepr] = vdom map {
    case TagNode(name, props, children, key) ⇒ TagRepr(name,
      props.collect({
        case NodeAttribute(attrName, value) ⇒ attrName → value
      }).toMap,
      props.collect({
        case e: NodeEventListener ⇒ e
      }).toSet,
      buildRepr(children)
    )
    case TextNode(s) ⇒ TextRepr(s)
    case f: FragmentNode ⇒ FragmentRepr(buildRepr(f.children))
    case cn: ComponentNode[_] ⇒
      val cached
  }
  */

}

case class DomReconciliationContext(container: raw.HTMLElement, eventDispatcher: EventDispatcher, currentVDom: Seq[Node])