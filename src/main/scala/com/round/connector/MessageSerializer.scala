package com.round
package connector

import akka.actor.ExtendedActorSystem
import akka.serialization.SerializerWithStringManifest

import com.google.protobuf.ByteString

class MessageSerializer(actorSystem: ExtendedActorSystem) {
  private lazy val serialization = akka.serialization.SerializationExtension(actorSystem)

  def toProtobuf(obj: AnyRef): MessageProtobuf = {
    val payloadSerializer = serialization.findSerializerFor(obj)
    val manifest = payloadSerializer match {
      case ser: SerializerWithStringManifest      => ser.manifest(obj).some.filter(_.nonEmpty)
      case _ if payloadSerializer.includeManifest => obj.getClass.getName.some
      case _                                      => None
    }
    val payloadBytes = ByteString.copyFrom(payloadSerializer.toBinary(obj))
    MessageProtobuf(payloadBytes, manifest.map(ByteString.copyFromUtf8), payloadSerializer.identifier.some)
  }

  def fromProtobuf(payload: ByteString, manfiest: Option[ByteString], sid: Int): AnyRef = {
    val clazz = manfiest.flatMap(m => actorSystem.dynamicAccess.getClassFor[AnyRef](m.toStringUtf8).toOption)
    serialization.deserialize[AnyRef](payload.toByteArray, sid, clazz).get
  }
}
