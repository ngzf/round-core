package com.round.system.core

trait HasId[A <: HasId[A]] {
  def id: Identifier[A]
}
