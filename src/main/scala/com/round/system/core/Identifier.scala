package com.round.system.core

import java.util.UUID
import java.nio.ByteBuffer

/**
 * Universally unique identifiers.
 */
trait Identifier[A <: HasId[A]] extends HasId[A] {
  override final val id = this
  final lazy val uuid = if (counters.isEmpty) new UUID(msb, lsb) else sys.error("Sharding counters are not empty, cannot convert to UUID")

  def msb: Long
  def lsb: Long
  def datacenterId: Long
  def generatorId: Long
  def sequenceNr: Long
  def counter: Long
  def counters: Vector[Identifier.Counter]
  def base64: String
  def nextLevel[B <: HasId[B]](sequenceNr: Long, counter: Int): Identifier[B]
}

private[core] object Identifier {
  case class Counter(sequenceNr: Long, counter: Int) {
    def toBinary: Array[Byte] = ByteBuffer.allocate(8 + 4).putLong(sequenceNr).putInt(counter).array()
    override def toString: String = s"$sequenceNr:$counter"
  }
  object Counter {
    implicit def tuple2Counter(t: (Long, Int)): Counter = Counter(t._1, t._2)
    def fromBinary(bb: ByteBuffer): Counter = Counter(bb.getLong(), bb.getInt())
  }

  def apply[A <: HasId[A]](msb: Long, lsb: Long, counters: Vector[Counter] = Vector.empty): Identifier[A] =
    StandardIdentifier(msb, lsb, counters)

  def apply[A <: HasId[A]](datacenterId: Long, generatorId: Long, sequenceNr: Long, counter: Long, counters: Vector[Identifier.Counter]): StandardIdentifier[A] =
    StandardIdentifier(datacenterId, generatorId, sequenceNr, counter, counters)

  def apply[A <: HasId[A]](uuid: UUID, counters: Vector[Identifier.Counter]): StandardIdentifier[A] =
    StandardIdentifier(uuid.getMostSignificantBits, uuid.getLeastSignificantBits, counters)

  def apply[A <: HasId[A]](base64: String): StandardIdentifier[A] =
    StandardIdentifier(base64)
}
