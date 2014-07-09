package com.zitadelz.system.core

import language.higherKinds

import com.google.protobuf._

import net.sandrogrzicic.scalabuff.Message
import net.sandrogrzicic.scalabuff.Parser

import akka.actor.ActorRef

trait Sourcing[Command] {

  case class Envelope(payload: Command = dummy, sequenceNr: Long = 0L, senderStack: List[ActorRef] = Nil)

  def empty: Envelope = Envelope()

  protected def dummy: Command
}

object Sourcing extends Sourcing[Message[_] with Parser[_]] {
  type Command = Message[_] with Parser[_]
  type Envelope = super.Envelope

  protected def dummy: Command = new Message[Nothing] with Parser[Nothing] {
    // Members declared in com.google.protobuf.MessageLite.Builder
    def build(): MessageLite = ???
    def buildPartial(): MessageLite = ???
    def clear(): MessageLite.Builder = ???

    // Members declared in net.sandrogrzicic.scalabuff.MessageBuilder
    def isInitialized: Boolean = ???
    def mergeFrom(input: CodedInputStream, extensionRegistry: ExtensionRegistryLite): Nothing = ???
    def mergeFrom(message: Nothing): Nothing = ???
    def toByteArray(): Array[Byte] = ???

    // Members declared in com.google.protobuf.MessageLiteOrBuilder
    def getDefaultInstanceForType(): MessageLite = ???

    // Members declared in com.google.protobuf.Parser
    def parsePartialFrom(x$1: CodedInputStream, x$2: ExtensionRegistryLite): Nothing = ???
  }
}
