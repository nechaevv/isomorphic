package com.github.nechaevv.isomorphic.vdom

import com.github.nechaevv.isomorphic.ActionDispatcher
import com.github.nechaevv.isomorphic.api.WeakMap
import org.scalajs.dom.document
import org.scalajs.dom.raw._

import scala.scalajs.js

object DomReconciler {

  private val componentCache = new WeakMap[ComponentVNode[Any, VNode], VNode]
  private val nodeReprCache = new WeakMap[VNode, VNodeRepr]
  private val elementMapping = new WeakMap[VNodeRepr, Node]
  private val listenerMapping = new WeakMap[VNodeEventListener, js.Function1[Event, Unit]]
  private val rootVDom = new WeakMap[Node, ComponentVNode[Any, FragmentVNode]]

  trait VNodeRepr {
    def key: String
  }

  case class ElementRepr(name: String, attrs: Map[String, String], eventListeners: Set[VNodeEventListener],
                         children: Seq[VNode], key: String) extends VNodeRepr
  case class FragmentRepr(children: Seq[VNode], key: String) extends VNodeRepr
  case class TextRepr(text:String, key: String) extends VNodeRepr

  def reconcileRootComponent[S](rootElement: Node with ParentNode, vdomComp: ComponentVNode[S, FragmentVNode], eventDispatcher: ActionDispatcher): Unit = {
    println("Render")
    val vdomComponentCurr = rootVDom.get(rootElement).toOption.asInstanceOf[Option[ComponentVNode[S, FragmentVNode]]]
    val (vdom, vdomCurr) = evalComponent(vdomComp, vdomComponentCurr)
    if (!vdomCurr.exists(_ eq vdom)) {
      reconcileNodeSeq(vdomCurr.map(_.children).getOrElse(Nil), vdom.children, None, rootElement, eventDispatcher)
      rootVDom.set(rootElement, vdomComp.asInstanceOf[ComponentVNode[Any, FragmentVNode]])
    }
  }

  def evalComponent[S, N <: VNode](vnode: ComponentVNode[S, N], vnodeCurrOpt: Option[ComponentVNode[S, N]]): (N, Option[N]) = {
    (for {
      vnodeCurr ← vnodeCurrOpt if vnodeCurr.component == vnode.component
      cached ← componentCache.get(vnodeCurr.asInstanceOf[ComponentVNode[Any, VNode]]).toOption
    } yield (cached.asInstanceOf[N], vnodeCurr.state)) match {
      case Some((cached, prevState)) if prevState == vnode.state ⇒
        componentCache.set(vnode.asInstanceOf[ComponentVNode[Any, VNode]], cached)
        (cached, Some(cached))
      case other ⇒
        val result = vnode.component(vnode.state)
        componentCache.set(vnode.asInstanceOf[ComponentVNode[Any, VNode]], result)
        (result, other.map(_._1))
    }
  }

  private def unwrapComponent(vnode: VNode, vnodeCurr: Option[VNode]): (VNode, Option[VNode]) = vnode match {
    case cn: ComponentVNode[_, _] ⇒
      val (unwrapNode, unwrapCurr) = evalComponent(cn.asInstanceOf[ComponentVNode[Any, VNode]],
        vnodeCurr.asInstanceOf[Option[ComponentVNode[Any, VNode]]])
      unwrapComponent(unwrapNode, unwrapCurr)
    case _ ⇒ (vnode, vnodeCurr)
  }

  private def addEventListener(listener: VNodeEventListener, elem: Element, eventDispatcher: ActionDispatcher): Unit = {
    val eventListenerFn: js.Function1[Event, Unit] = e ⇒ listener.handler(e)
      .through(eventDispatcher.enqueue).compile.drain.unsafeRunAsyncAndForget()
    listenerMapping.set(listener, eventListenerFn)
    elem.addEventListener(
      listener.eventType.name.toLowerCase,
      eventListenerFn,
      listener.eventType.isCapturing
    )
  }

  private def elementProperties(modifiers: Seq[VNodeModifier]): (Map[String, String], Set[VNodeEventListener], Seq[VNode]) = {
    val (classes, attributes, listeners, children) = modifiers.foldLeft(
      (List.empty[String], List.empty[(String, String)], List.empty[VNodeEventListener], List.empty[VNode])
    ) { (acc, modifier) ⇒
      val (clss, attrs, lsnrs, chldrn) = acc
      modifier match {
        case VNodeClass(name) ⇒ (name :: clss, attrs, lsnrs, chldrn)
        case VNodeAttribute(attrName, value) ⇒ (clss, (attrName, value) :: attrs, lsnrs, chldrn)
        case e: VNodeEventListener ⇒ (clss, attrs, e :: lsnrs, chldrn)
        case VNodeChild(childNode) ⇒ (clss, attrs, lsnrs, childNode :: chldrn)
        case _ ⇒ acc
      }
    }
    (
      (if (classes.nonEmpty) ("class", classes.mkString(" ")) :: attributes else attributes).toMap,
      listeners.toSet,
      children.reverse
    )
  }

  def syncAttributeChange(elem: Element, name: String, value: Option[String]): Unit = {
    elem match {
      case input: HTMLInputElement ⇒ name match {
        case "value" ⇒
          val newValue = value.getOrElse("")
          if (input.value != newValue) input.value = newValue
        case "checked" if input.`type` == "checkbox" ⇒
          if (input.checked != value.nonEmpty) input.checked = value.nonEmpty
        case _ ⇒
      }
      case _ ⇒
    }
  }

  private def reconcileNode(vnode: VNode, nodeIndex: Int, offset: Option[Int], container: Node with ParentNode, key: String,
                            matchingNodeOpt: Option[(VNode, VNodeRepr)], eventDispatcher: ActionDispatcher): Int = {
    val containerIndex = offset.getOrElse(0) + nodeIndex
    val (node, vnodeRepr, nodeCount) = vnode match {
      case _: ComponentVNode[_, _] ⇒ throw new RuntimeException("Component node not expected")
      case tn @ ElementVNode(name, modifiers, _) ⇒
        (for {
          (tagNodeCurr: ElementVNode, tagReprCurr: ElementRepr) ← matchingNodeOpt if tagReprCurr.name == name
          domNodeCurr ← elementMapping.get(tagReprCurr).toOption
        } yield {
          if (tagNodeCurr eq tn) (domNodeCurr, tagReprCurr, 1)
          else {
            val elem = domNodeCurr.asInstanceOf[Element]
            val (attributes, listeners, children) = elementProperties(modifiers)
            for ((k, v) ← attributes) if (!tagReprCurr.attrs.get(k).contains(v)) {
              println(s"+attr($name, $k, $v)")
              elem.setAttribute(k, v)
              syncAttributeChange(elem, k, Some(v))
            }
            for (k ← tagReprCurr.attrs.keys) if (!attributes.contains(k)) {
              println(s"-attr($name, $k)")
              elem.removeAttribute(k)
              syncAttributeChange(elem, k, None)
            }
            for (lsn ← listeners) if (!tagReprCurr.eventListeners.contains(lsn)) {
              println(s"+listener($name,${lsn.eventType.name})")
              addEventListener(lsn, elem, eventDispatcher)
            }
            for (lsn ← tagReprCurr.eventListeners) if (!listeners.contains(lsn)) {
              println(s"-listener($name,${lsn.eventType.name})")
              elem.removeEventListener(lsn.eventType.name.toLowerCase, listenerMapping.get(lsn).get, lsn.eventType.isCapturing)
            }
            reconcileNodeSeq(tagReprCurr.children, children, None, elem, eventDispatcher)
            (elem, ElementRepr(name, attributes, listeners, children, key), 1)
          }
        }).getOrElse({
          println(s"+$name")
          val elem = document.createElement(name)
          val (attributes, listeners, children) = elementProperties(modifiers)
          for ((k, v) ← attributes) elem.setAttribute(k, v)
          for (lsn ← listeners) addEventListener(lsn, elem, eventDispatcher)
          reconcileNodeSeq(Nil, children, None, elem, eventDispatcher)
          (elem, ElementRepr(name, attributes, listeners, children, key), 1)
        })

      case fn: FragmentVNode ⇒
        (for {
          (fragNode: FragmentVNode, fragNodeRepr: FragmentRepr) ← matchingNodeOpt
        } yield {
          val nodeCount = reconcileNodeSeq(fragNode.children, fn.children, Some(containerIndex), container, eventDispatcher)
          (container, FragmentRepr(fn.children, key), nodeCount)
        }).getOrElse({
          println("+$fragment")
          val nodeCount = reconcileNodeSeq(Nil, fn.children, Some(containerIndex), container, eventDispatcher)
          (container, FragmentRepr(fn.children, key), nodeCount)
        })

      case TextVNode(text) ⇒
        (for {
          (_, textNodeRepr: TextRepr) ← matchingNodeOpt
          domNode ← elementMapping.get(textNodeRepr).toOption if domNode.isInstanceOf[Text]
        } yield {
          if (textNodeRepr.text != text) {
            println(s"text=$text")
            domNode.asInstanceOf[Text].data = text
            val repr = TextRepr(text, key)
            elementMapping.set(repr, domNode)
            (domNode, repr, 1)
          } else (domNode, textNodeRepr, 1)
        }).getOrElse({
          println(s"+text=$text")
          (document.createTextNode(text), TextRepr(text, key), 1)
        })
    }

    nodeReprCache.set(vnode, vnodeRepr)

    if (!vnode.isInstanceOf[FragmentVNode]) {
      elementMapping.set(vnodeRepr, node)
      if (containerIndex >= container.childElementCount) container.appendChild(node)
      else {
        val existingNode = container.childNodes(containerIndex)
        if (!(existingNode eq node)) container.insertBefore(node, existingNode)
      }
    }

    nodeCount
  }

  private def reconcileNodeSeq(currentVDom: Seq[VNode], vdom: Seq[VNode], offset: Option[Int], container: Node with ParentNode,
                               eventDispatcher: ActionDispatcher): Int = {
    var auxId = 0
    def nodeKey(n: VNode) = n.key.getOrElse({ auxId += 1; "$" + auxId.toString})
    val vdomCurrMap = currentVDom.map(ni ⇒ nodeKey(ni) → ni).toMap

    auxId = 0
    var index = 0
    for (vnode ← vdom) {
      val key = nodeKey(vnode)
      val vnodeCurrOpt = vdomCurrMap.get(key)
      val (vnodeUnwrapped, vnodeCurrUnwrappedOpt) = unwrapComponent(vnode, vnodeCurrOpt)
      val matchingNodeOpt = for {
        vnodeCurrUnwrapped ← vnodeCurrUnwrappedOpt
        reprCurr ← nodeReprCache.get(vnodeCurrUnwrapped).toOption
      } yield (vnodeCurrUnwrapped, reprCurr)
      val nodeCount = reconcileNode(vnodeUnwrapped, index, offset, container, key, matchingNodeOpt, eventDispatcher)
      index += nodeCount
    }

    if (offset.isEmpty) {
      for (i ← index until container.childElementCount) {
        println("-")
        container.removeChild(container.lastChild)
      }
    }

    index
  }

}
