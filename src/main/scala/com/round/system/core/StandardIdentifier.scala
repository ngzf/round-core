package com.round.system.core

import java.nio.ByteBuffer
import java.util.UUID

import org.apache.commons.codec.binary.Base64

/**
 * The standard type of [[Identifier]].
 */
private[core] case class StandardIdentifier[A <: HasId[A]](msb: Long, lsb: Long, counters: Vector[Identifier.Counter] = Vector.empty) extends Identifier[A] {
  import StandardIdentifier._

  def copy[B <: HasId[B]](datacenterId: Long = datacenterId, generatorId: Long = generatorId, sequenceNr: Long = sequenceNr, counter: Long = counter, counters: Vector[Identifier.Counter] = counters): StandardIdentifier[B] =
    StandardIdentifier(datacenterId, generatorId, sequenceNr, counter, counters)

  override def datacenterId: Long = (msb & datacenterIdMask) >> datacenterIdIndex
  override def generatorId: Long = (msb & generatorIdMask) >> generatorIdIndex
  override def sequenceNr: Long = lsb
  override def counter: Long = (msb & counterMask) >> counterIndex

  override lazy val base64: String = encoder.encodeAsString {
    if (toBinary.length == StandardIdentifier.numberOfEssentialBytes) toBinary.init /* empty counters - adapt to the existing ones */
    else toBinary
  }

  private lazy val toBinary = {
    require(counters.length <= Byte.MaxValue, s"Currently support only IDs have up to ${Byte.MaxValue} levels")
    val cs = counters.map(_.toBinary)
    val length = 8 + 8 + (0 /: cs) { _ + _.size } + 1
    val bb = ByteBuffer.allocate(length).putLong(msb).putLong(lsb)
    bb.put(cs.length.toByte)
    for (a <- cs; c <- a) bb.put(c)
    bb.array()
  }

  override def nextLevel[B <: HasId[B]](sequenceNr: Long, counter: Int): StandardIdentifier[B] =
    copy(counters = counters :+ Identifier.Counter(sequenceNr, counter))

  override def toString: String =
    s"$base64-[datacenterId: $datacenterId, generatorId: $generatorId, sequenceNr: $sequenceNr, counter: $counter, ${counters.mkString("[", ",", "]")}]"
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
  val leastNumberOfBytes = 8 + 8
  val numberOfEssentialBytes = leastNumberOfBytes + 1

  private def mask(index: Long, length: Long): Long =
    ((1 << length) - 1) << index

  def apply[A <: HasId[A]](datacenterId: Long, generatorId: Long, sequenceNr: Long, counter: Long, counters: Vector[Identifier.Counter]): StandardIdentifier[A] =
    if (counter <= maxCounter) {
      StandardIdentifier((datacenterId << datacenterIdIndex) + (generatorId << generatorIdIndex) + (counter << counterIndex), sequenceNr, counters)
    } else {
      throw new Error(s"Identifier pool exhauted for datacenterId: $datacenterId generatorId $generatorId sequenceNr: $sequenceNr")
    }

  def apply[A <: HasId[A]](uuid: UUID, counters: Vector[Identifier.Counter]): StandardIdentifier[A] =
    StandardIdentifier(uuid.getMostSignificantBits, uuid.getLeastSignificantBits, counters)

  def apply[A <: HasId[A]](base64: String): StandardIdentifier[A] = {
    val b = encoder.decode(base64)
    require(b.length >= leastNumberOfBytes, s"fromBinary($base64) - length ${b.length}")
    val bb = ByteBuffer.wrap(b)
    val msb = bb.getLong()
    val lsb = bb.getLong()
    val counters =
      if (bb.hasRemaining) {
        val cs = Vector.newBuilder[Identifier.Counter]
        val csLength = bb.get()
        for (_ <- 0 until csLength) cs += Identifier.Counter.fromBinary(bb)
        cs.result()
      } else { // adapt to the old existing IDs
        Vector.empty
      }
    StandardIdentifier(msb, lsb, counters)
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
