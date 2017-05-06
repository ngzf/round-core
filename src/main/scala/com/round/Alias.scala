package com.round

import scala.language.implicitConversions

trait Alias {
  type -> [+A, +B] = (A, B)
  object -> { @inline def unapply[A, B](t: A -> B): Option[A -> B] = Tuple2.unapply(t) }

  type Validation[+A] = scalaz.Validation[List[String], A]

  implicit def toListOps[A](a: A): ListOps[A] = new ListOps(a)
}

final class ListOps[A](val self: A) extends AnyVal {
  final def wrapNel: List[A] = List(self)
}
