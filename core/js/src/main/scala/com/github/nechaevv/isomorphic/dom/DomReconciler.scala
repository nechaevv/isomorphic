package com.github.nechaevv.isomorphic.dom

import com.github.nechaevv.isomorphic.EventDispatcher
import com.github.nechaevv.isomorphic.api.WeakMap
import org.scalajs.dom.document
import org.scalajs.dom.raw

import scala.scalajs.js

object DomReconciler {

  private val componentCache = new WeakMap[ComponentNode[Any, Node], Node]
  private val nodeReprCache = new WeakMap[Node, NodeRepr]
  private val elementMapping = new WeakMap[NodeRepr, raw.Node]
  private val listenerMapping = new WeakMap[NodeEventListener, js.Function1[raw.Event, Unit]]
  private val rootVDom = new WeakMap[raw.Node, ComponentNode[Any, FragmentNode]]

  trait NodeRepr {
    def key: String
  }

  case class TagRepr(name: String, attrs: Map[String, String], eventListeners: Set[NodeEventListener],
                     children: Seq[NodeRepr], key: String) extends NodeRepr
  case class FragmentRepr(children: Seq[NodeRepr], key: String) extends NodeRepr
  case class TextRepr(text:String, key: String) extends NodeRepr

  def reconcileRootComponent[S](rootElement: raw.Node with raw.ParentNode, vdomComp: ComponentNode[S, FragmentNode], eventDispatcher: EventDispatcher): Unit = {
    val vdomComponentCurr = rootVDom.get(rootElement).toOption.asInstanceOf[Option[ComponentNode[S, FragmentNode]]]
    val (vdom, vdomCurr) = evalComponent(vdomComp, vdomComponentCurr)
    if (!vdomCurr.exists(_ eq vdom)) {
      reconcileNodeSeq(vdomCurr.map(_.children).getOrElse(Nil), vdom.children, rootElement, eventDispatcher)
      rootVDom.set(rootElement, vdomComp.asInstanceOf[ComponentNode[Any, FragmentNode]])
    }
  }

  def evalComponent[S, N <: Node](vnode: ComponentNode[S, N], vnodeCurrOpt: Option[ComponentNode[S, N]]): (N, Option[N]) = {
    (for {
      vnodeCurr ← vnodeCurrOpt
      cached ← componentCache.get(vnodeCurr.asInstanceOf[ComponentNode[Any, Node]]).toOption
    } yield (cached.asInstanceOf[N], vnodeCurr.state)) match {
      case Some((cached, prevState)) if prevState == vnode.state ⇒
        componentCache.set(vnode.asInstanceOf[ComponentNode[Any, Node]], cached)
        (cached, Some(cached))
      case other ⇒
        val result = vnode.component(vnode.state)
        componentCache.set(vnode.asInstanceOf[ComponentNode[Any, Node]], result)
        (result, other.map(_._1))
    }
  }

  private def unwrapComponent[S, N <: Node](vnode: Node, vnodeCurr: Option[Node]): (Node, Option[Node]) = vnode match {
    case cn: ComponentNode[S, N] ⇒
      val componentNodeCurr = vnodeCurr.asInstanceOf[Option[ComponentNode[S, N]]]
      val (unwrapNode, unwrapCurr) = evalComponent(cn, componentNodeCurr)
      unwrapComponent(unwrapNode, unwrapCurr)
    case _ ⇒ (vnode, vnodeCurr)
  }

  private def addEventListener(listener: NodeEventListener, elem: raw.Element, eventDispatcher: EventDispatcher): Unit = {
    val eventListenerFn = (e: raw.Event) ⇒ listener.handler(e).through(eventDispatcher.enqueue).compile.drain
      .unsafeRunAsyncAndForget()
    listenerMapping.set(listener, eventListenerFn)
    elem.addEventListener(
      listener.eventType.name.toLowerCase,
      eventListenerFn,
      listener.eventType.isCapturing
    )
  }

  private def tagProps(props: Seq[NodeProperty]): (Map[String, String], Set[NodeEventListener]) = {
    (
      props.collect({
        case NodeAttribute(attrName, value) ⇒ attrName → value
      }).toMap,
      props.collect({
        case e: NodeEventListener ⇒ e
      }).toSet
    )
  }

  private def reconcileNode(vnode: Node, nodeIndex: Int,  container: raw.Node with raw.ParentNode, key: String,
                            matchingNodeOpt: Option[(Node, NodeRepr, Int)], eventDispatcher: EventDispatcher): NodeRepr = {

    val (node, vnodeRepr) = vnode match {
      case _: ComponentNode[_, _] ⇒ throw new RuntimeException("Component node not expected")
      case tn @ TagNode(name, props, children, _) ⇒
        (for {
          (tagNodeCurr: TagNode, tagReprCurr: TagRepr, tagIndexCurr) ← matchingNodeOpt if tagReprCurr.name == name
          domNodeCurr ← elementMapping.get(tagReprCurr).toOption
        } yield {
          if (tagNodeCurr eq tn) (domNodeCurr, tagReprCurr)
          else {
            val elem = domNodeCurr.asInstanceOf[raw.Element]
            val (attrs, listeners) = tagProps(props)
            for ((k, v) ← attrs) if (!tagReprCurr.attrs.get(k).contains(v)) elem.setAttribute(k, v)
            for (k ← tagReprCurr.attrs.keys) if (!attrs.contains(k)) elem.removeAttribute(k)
            for (lsn ← listeners) if (!tagReprCurr.eventListeners.contains(lsn)) addEventListener(lsn, elem, eventDispatcher)
            for (lsn ← tagReprCurr.eventListeners) if (!listeners.contains(lsn)) {
              elem.removeEventListener(lsn.eventType.name.toLowerCase, listenerMapping.get(lsn).get, lsn.eventType.isCapturing)
            }
            (elem, TagRepr(name, attrs, listeners, reconcileNodeSeq(tagNodeCurr.children, children, elem, eventDispatcher), key))
          }
        }).getOrElse({
          println(s"+$name")
          val elem = document.createElement(name)
          val (attrs, listeners) = tagProps(props)
          for ((k, v) ← attrs) elem.setAttribute(k, v)
          for (lsn ← listeners) addEventListener(lsn, elem, eventDispatcher)
          (elem, TagRepr(name, attrs, listeners, reconcileNodeSeq(Nil, children, elem, eventDispatcher), key))
        })

      case fn @ FragmentNode(children, _) ⇒ //TODO: unwrap DIV
        (for {
          (fragNode: FragmentNode, fragNodeRepr: FragmentRepr, tagNodeIndex) ← matchingNodeOpt
          domNode ← elementMapping.get(fragNodeRepr).toOption
          if domNode.isInstanceOf[raw.Element] && domNode.asInstanceOf[raw.Element].tagName == "DIV"
        } yield {
          if (fragNode == fn) (domNode, fragNodeRepr)
          else (domNode, FragmentRepr(reconcileNodeSeq(fragNode.children, children, domNode.asInstanceOf[raw.Element], eventDispatcher), key))
        }).getOrElse({
          println("+$fragment")
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
        }).getOrElse({
          println("+$text")
          (document.createTextNode(text), TextRepr(text, key))
        })
    }

    nodeReprCache.set(vnode, vnodeRepr)
    elementMapping.set(vnodeRepr, node)

    if (nodeIndex >= container.childElementCount) container.appendChild(node)
    else {
      val existingNode = container.childNodes(nodeIndex)
      if (!(existingNode eq node)) container.insertBefore(node, existingNode)
    }

    vnodeRepr
  }

  private def reconcileNodeSeq(currentVDom: Seq[Node], vdom: Seq[Node], container: raw.Node with raw.ParentNode,
                               eventDispatcher: EventDispatcher): Seq[NodeRepr] = {
    var auxId = 0
    def nodeKey(n: Node) = n.key.getOrElse({ auxId += 1; "$" + auxId.toString})
    val vdomCurrMap = currentVDom.zipWithIndex.map(ni ⇒ nodeKey(ni._1) → ni).toMap

    auxId = 0
    val vdomRepr = for ((vnode, nodeIndex) ← vdom.zipWithIndex) yield {
      val key = nodeKey(vnode)
      val vnodeCurrOpt = vdomCurrMap.get(key)
      val (vnodeUnwrapped, vnodeCurrUnwrappedOpt) = unwrapComponent(vnode, vnodeCurrOpt.map(_._1))
      val matchingNodeOpt = for {
        (_, vnodeCurrIndex) ← vnodeCurrOpt
        vnodeCurrUnwrapped ← vnodeCurrUnwrappedOpt
        reprCurr ← nodeReprCache.get(vnodeCurrUnwrapped).toOption
      } yield (vnodeCurrUnwrapped, reprCurr, vnodeCurrIndex)
      reconcileNode(vnodeUnwrapped, nodeIndex, container, key, matchingNodeOpt, eventDispatcher)
    }

    for (i ← vdom.length until container.childElementCount) {
      println("-")
      container.removeChild(container.lastChild)
    }
    vdomRepr
  }

}