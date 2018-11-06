package com.github.nechaevv.isomorphic

import cats.effect.IO
import io.circe._
import io.circe.parser._
import org.scalajs.dom.experimental.{HeadersInit, Response}

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.|

object FetchIO {

  implicit def responsePromiseToTextIO(promise: js.Promise[Response]): IO[String] = IO.async { cb ⇒
    val errorHandler: js.Function1[Any, Unit | js.Thenable[Unit]] = (error: Any) ⇒ cb(Left(new RuntimeException(error.toString)))
    promise
      .`then`[String](response ⇒ if (response.ok) response.text()
    else throw new HttpException(response.status), js.undefined)
      .`then`[Unit](content ⇒ cb(Right(content)), errorHandler)
  }

  implicit class decodableTextPromise(promise: js.Promise[Response]) {
    def as[T](implicit decoder: Decoder[T]): IO[T] = responsePromiseToTextIO(promise)
      .flatMap(responseText ⇒ decode[T](responseText).fold(IO.raiseError, IO.pure))
  }

  val jsonContentType: HeadersInit = js.Dictionary("Content-Type" → "application/json")

}

class HttpException(val code: Int) extends RuntimeException(s"HTTP non-success code error: $code")