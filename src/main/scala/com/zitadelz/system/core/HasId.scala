package com.zitadelz.system.core

trait HasId[A <: HasId[A]] {
  def id: Identifier[A]
}
