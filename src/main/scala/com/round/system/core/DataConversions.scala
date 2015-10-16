package com.round.system.core

import com.google.protobuf.MessageLite

import net.sandrogrzicic.scalabuff.Message

object Predef {
  private[core]type Protuf[P <: Protuf[P]] = MessageLite with MessageLite.Builder with Message[P] {
    def msb: Long
    def lsb: Long
  }
}

trait ProtufConv[I <: HasId[I], P <: Predef.Protuf[P]] {
  def protuf(msb: Long, lsb: Long): P
}

case class WithPrefix[T](prefix: Int) extends AnyVal
