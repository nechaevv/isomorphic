package com.github.nechaevv.isomorphic.dom

import com.github.nechaevv.isomorphic.EventDispatcher
import com.github.nechaevv.isomorphic.api.WeakMap
import org.scalajs.dom.document
import org.scalajs.dom.raw

import scala.scalajs.js

object DomReconciler {

  private val componentCache = new WeakMap[ComponentNode[_], Node]
  private val nodeReprCache = new WeakMap[Node, NodeRepr]
  private val elementMapping = new WeakMap[NodeRepr, raw.Node]
  private val listenerMapping = new WeakMap[NodeEventListener, js.Function1[raw.Event, Unit]]
  private val rootVDom = new WeakMap[raw.HTMLElement, Node]

  trait NodeRepr {
    def key: String
  }

  case class TagRepr(name: String, attrs: Map[String, String], eventListeners: Set[NodeEventListener],
                     children: Seq[NodeRepr], key: String) extends NodeRepr
  case class FragmentRepr(children: Seq[NodeRepr], key: String) extends NodeRepr
  case class TextRepr(text:String, key: String) extends NodeRepr

  def apply(rootElement: raw.HTMLElement, vdom: Node, eventDispatcher: EventDispatcher): Unit = {
    val matchingNode = (for {
      vdom ← rootVDom.get(rootElement).toOption
      vdomRepr ← nodeReprCache.get(vdom).toOption
    } yield (vdom, vdomRepr, 0)).getOrElse({
      val attributes = (for (i ← 0 until rootElement.attributes.length) yield {
        val attr = rootElement.attributes(i)
        attr.name → attr.value
      }).toMap
      (
        TagNode(rootElement.tagName),
        TagRepr(rootElement.tagName, attributes, Set.empty, Seq.empty, ""),
        0
      )
    })

    reconcileNode(vdom, 0, rootElement.parentElement, "", Some(matchingNode), eventDispatcher)
    rootVDom.set(rootElement, vdom)
  }

  def evalComponent[S](cn: ComponentNode[S]): Node = {
    val cached = componentCache.get(cn)
    if (cached.isDefined) cached.get
    else {
      val result = cn.component(cn.state)
      componentCache.set(cn, result)
      result
    }
  }

  private def reconcileNode(vnode: Node, nodeIndex: Int, container: raw.Element, key: String,
                    matchingNodeOpt: Option[(Node, NodeRepr, Int)], eventDispatcher: EventDispatcher): NodeRepr = {

    def unwrapComponent(n: Node): Node = n match {
      case cn: ComponentNode[_] ⇒ unwrapComponent(evalComponent(cn))
      case _ ⇒ n
    }

    def tagProps(props: Seq[NodeProperty]): (Map[String, String], Set[NodeEventListener]) = {
      (
        props.collect({
          case NodeAttribute(attrName, value) ⇒ attrName → value
        }).toMap,
        props.collect({
          case e: NodeEventListener ⇒ e
        }).toSet
      )
    }

    def addEventListener(listener: NodeEventListener, elem: raw.Element): Unit = {
      val eventListenerFn = (e: raw.Event) ⇒ listener.handler(e).through(eventDispatcher.enqueue).compile.drain.unsafeRunAsyncAndForget()
      listenerMapping.set(listener, eventListenerFn)
      elem.addEventListener(
        listener.eventType.name.toLowerCase,
        eventListenerFn,
        listener.eventType.isCapturing
      )
    }

    val (node, vnodeRepr) = unwrapComponent(vnode) match {
      case _: ComponentNode[_] ⇒ throw new RuntimeException("Component node not expected")
      case TagNode(name, props, children, _) ⇒
        (for {
          (tagNode: TagNode, tagNodeRepr: TagRepr, tagNodeIndex) ← matchingNodeOpt
          domNode ← elementMapping.get(tagNodeRepr).toOption
          if domNode.isInstanceOf[raw.Element] && domNode.asInstanceOf[raw.Element].tagName.equalsIgnoreCase(name)
        } yield {
          if (tagNode eq vnode) (domNode, tagNodeRepr)
          else {
            val elem = domNode.asInstanceOf[raw.Element]
            val (attrs, listeners) = tagProps(props)
            for ((k, v) ← attrs) if (!tagNodeRepr.attrs.get(k).contains(v)) elem.setAttribute(k, v)
            for (k ← tagNodeRepr.attrs.keys) if (!attrs.contains(k)) elem.removeAttribute(k)
            for (lsn ← listeners) if (!tagNodeRepr.eventListeners.contains(lsn)) addEventListener(lsn, elem)
            for (lsn ← tagNodeRepr.eventListeners) if (!listeners.contains(lsn)) {
              elem.removeEventListener(lsn.eventType.name.toLowerCase, listenerMapping.get(lsn).get, lsn.eventType.isCapturing)
            }
            (elem, TagRepr(name, attrs, listeners, reconcileNodeSeq(tagNode.children, children, elem, eventDispatcher), key))
          }
        }).getOrElse({
          val elem = document.createElement(name)
          val (attrs, listeners) = tagProps(props)
          for ((k, v) ← attrs) elem.setAttribute(k, v)
          for (lsn ← listeners) addEventListener(lsn, elem)
          (elem, TagRepr(name, attrs, listeners, reconcileNodeSeq(Nil, children, elem, eventDispatcher), key))
        })

      case FragmentNode(children, _) ⇒ //TODO: unwrap DIV
        (for {
          (fragNode: FragmentNode, fragNodeRepr: FragmentRepr, tagNodeIndex) ← matchingNodeOpt
          domNode ← elementMapping.get(fragNodeRepr).toOption
          if domNode.isInstanceOf[raw.Element] && domNode.asInstanceOf[raw.Element].tagName == "DIV"
        } yield {
          if (fragNode == vnode) (domNode, fragNodeRepr)
          else (domNode, FragmentRepr(reconcileNodeSeq(fragNode.children, children, domNode.asInstanceOf[raw.Element], eventDispatcher), key))
        }).getOrElse({
          val elem = document.createElement("DIV")
          (elem, FragmentRepr(reconcileNodeSeq(Nil, children, elem, eventDispatcher), key))
        })

      case TextNode(text) ⇒
        (for {
          (textNode: TextNode, textNodeRepr: TextRepr, textNodeIndex) ← matchingNodeOpt
          domNode ← elementMapping.get(textNodeRepr).toOption if domNode.isInstanceOf[raw.Text]
        } yield {
          if (textNodeRepr.text != text) {
            domNode.asInstanceOf[raw.Text].data = text
            val repr = TextRepr(text, key)
            elementMapping.set(repr, domNode)
            (domNode, repr)
          } else (domNode, textNodeRepr)
        }).getOrElse((document.createTextNode(text), TextRepr(text, key)))
    }

    elementMapping.set(vnodeRepr, node)
    nodeReprCache.set(vnode, vnodeRepr)

    if (nodeIndex >= container.childElementCount) container.appendChild(node)
    else {
      val existingNode = container.childNodes(nodeIndex)
      if (!(existingNode eq node)) container.insertBefore(node, existingNode)
    }
    vnodeRepr
  }

  private def reconcileNodeSeq(currentVDom: Seq[Node], vdom: Seq[Node], container: raw.Element,
                               eventDispatcher: EventDispatcher): Seq[NodeRepr] = {
    val currentVdomRepr = currentVDom.map(vnode ⇒ (vnode, nodeReprCache.get(vnode))).zipWithIndex.collect({
      case ((vnode, vnodeReprOpt), index) if vnodeReprOpt.isDefined  ⇒
        val nodeRepr = vnodeReprOpt.get
        nodeRepr.key → (vnode, nodeRepr, index)
    }).toMap
    var auxId = 0
    val vdomRepr = for ((vnode, nodeIndex) ← vdom.zipWithIndex) yield {
      val key = vnode.key.getOrElse({ auxId += 1; "$" + auxId.toString})
      reconcileNode(vnode, nodeIndex, container, key, currentVdomRepr.get(key), eventDispatcher)
    }
    for (i ← vdom.length until container.childElementCount) {
      container.removeChild(container.childNodes(i))
    }
    vdomRepr
  }

}