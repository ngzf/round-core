package com.zitadelz.system.core

import language.higherKinds

import net.sandrogrzicic.scalabuff.Message
import net.sandrogrzicic.scalabuff.Parser

import akka.actor.ActorRef

trait Sourcing[Command] {
  case class Envelope(payload: Command, sequenceNr: Long, senderStack: List[ActorRef])
}

object Sourcing extends Sourcing[Message[_] with Parser[_]] {
  type Command = Message[_] with Parser[_]
  type Envelope = super.Envelope
}
