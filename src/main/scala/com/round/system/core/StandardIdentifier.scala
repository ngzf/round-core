package com.round.system.core

import java.nio.ByteBuffer
import java.util.UUID

import org.apache.commons.codec.binary.Base64

/**
 * The standard type of [[Identifier]].
 */
private[core] case class StandardIdentifier[A <: HasId[A]](msb: Long, lsb: Long) extends Identifier[A] {
  import StandardIdentifier._

  override def datacenterId: Long = (msb & datacenterIdMask) >> datacenterIdIndex
  override def generatorId: Long = (msb & generatorIdMask) >> generatorIdIndex
  override def sequenceNr: Long = lsb
  override def counter: Long = (msb & counterMask) >> counterIndex
  override lazy val base64: String = encoder.encodeAsString(ByteBuffer.allocate(16).putLong(msb).putLong(lsb).array())

  override def toString: String =
    s"StandardIdentifier { datacenterId: $datacenterId, generatorId: $generatorId, sequenceNr: $sequenceNr, counter: $counter, base64: $base64 }"
}

/**
 * The standard representation of a [[Identifier]]. Format is as following:
 *
 * | Most significant bits                           | Least significant bits
 * | 64 bits                                         | 64 bits
 * |-------------------------------------------------|-----------------------
 * | Reserved | datacenterId | generatorID | counter | sequenceNr
 * | 40 bits  | 9 bits       | 5 bits      | 10 bits | 64 bits
 */
private[core] object StandardIdentifier {
  val encoder = new Base64(-1, Array[Byte](), true)

  private def mask(index: Long, length: Long): Long =
    ((1 << length) - 1) << index

  def apply[A <: HasId[A]](datacenterId: Long, generatorId: Long, sequenceNr: Long, counter: Long): StandardIdentifier[A] =
    if (counter <= maxCounter) {
      StandardIdentifier((datacenterId << datacenterIdIndex) + (generatorId << generatorIdIndex) + (counter << counterIndex), sequenceNr)
    } else {
      throw new Error(s"Identifier pool exhauted for datacenterId: $datacenterId generatorId $generatorId sequenceNr: $sequenceNr")
    }

  def apply[A <: HasId[A]](uuid: UUID): StandardIdentifier[A] =
    StandardIdentifier(uuid.getMostSignificantBits, uuid.getLeastSignificantBits)

  def apply[A <: HasId[A]](base64: String): StandardIdentifier[A] = {
    val bb = ByteBuffer.wrap(encoder.decode(base64))
    StandardIdentifier(bb.getLong(), bb.getLong())
  }

  val counterIndex = 0
  val counterLength = 10
  val counterMask = mask(counterIndex, counterLength)
  val maxCounter = math.pow(2, counterLength)

  val generatorIdIndex = counterIndex + counterLength
  val generatorIdLength = 5
  val generatorIdMask = mask(generatorIdIndex, generatorIdLength)

  val datacenterIdIndex = generatorIdIndex + generatorIdLength
  val datacenterIdLength = 9
  val datacenterIdMask = mask(datacenterIdIndex, datacenterIdLength)

  val reservedIndex = datacenterIdIndex + datacenterIdLength
  val reservedLength = 40
  val reservedMask = mask(datacenterIdIndex, datacenterIdLength)
}
