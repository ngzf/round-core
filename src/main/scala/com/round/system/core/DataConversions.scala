package com.round.system.core

import com.trueaccord.scalapb._

object IDMapper {
  def apply[I <: HasId[I], P <: IdProtuf[I, P]](baseToCustom: Identifier[I] => P): TypeMapper[Identifier[I], P] =
    TypeMapper(baseToCustom)(p => Identifier[I](p.msb, p.lsb))
}

trait Protuf[P <: Protuf[P]] extends GeneratedMessage with Message[P]

trait IdProtuf[I <: HasId[I], P <: IdProtuf[I, P]] extends Protuf[P] with GeneratedMessage with Message[P] {
  def msb: Long
  def lsb: Long
}

case class WithPrefix[T](prefix: Int) extends AnyVal
