package com.zitadelz.system.core

trait Sourcing {
  type Persisting <: {
    def payload: Any
    def sequenceNr: Long
  }
}

object Sourcing extends Sourcing {
  type Command = net.sandrogrzicic.scalabuff.Message[_] with net.sandrogrzicic.scalabuff.Parser[_]
  case class Persisting(payload: Any, sequenceNr: Long)
}
