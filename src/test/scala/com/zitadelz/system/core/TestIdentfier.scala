package com.zitadelz.system.core

object TestIdentifier {
  def apply[A <: HasId[A]]: Identifier[A] = Identifier[A](0, 0)
}
