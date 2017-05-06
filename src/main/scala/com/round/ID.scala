package com.round

import java.nio.ByteBuffer
import java.util.UUID

import org.apache.commons.codec.binary.Base64

import com.trueaccord.scalapb.TypeMapper

case class ID(msb: Long, lsb: Long) {
  lazy val uuid = new UUID(msb, lsb)
  lazy val binary = ByteBuffer.allocate(8 + 8).putLong(msb).putLong(lsb).array()
  lazy val base64 = ID.encoder.encodeAsString(binary)

  def timestamp: Long = msb >> ID.timestampShift & ID.timestampMask
  def generatorNr: Long = msb >> ID.generatorNrShift & ID.generatorNrMask
  def counter: Long = msb >> ID.counterShift & ID.counterMask
  def sequenceNr: Long = lsb >> ID.sequenceNrShift & ID.sequenceNrMask
  def randomSeed: Long = lsb >> ID.randomSeedShift & ID.randomSeedMask

  def copy(timestamp: Long = timestamp, generatorNr: Long = generatorNr, sequenceNr: Long = sequenceNr, counter: Long = counter, randomSeed: Long = randomSeed): ID =
    ID(timestamp, generatorNr, sequenceNr, counter, randomSeed)

  override def toString(): String = s"ID($base64|$timestamp|$generatorNr|$sequenceNr|$counter|$randomSeed)"
}

/**
 * | 128 bits of an ID                                                      |
 * | most significant 64 bits          | least significant 64 bits          |
 * | timestamp | generatorNr | counter | reserved | sequenceNr | randomSeed |
 * | 42 bits   | 10 bits     | 12 bits | 12 bits  | 20 bits    | 32 bits    |
 */
object ID {
  val encoder = new Base64(-1, Array.empty[Byte], true)

  implicit val typeMapper = TypeMapper[String, ID](apply)(_.base64)

  /**
   * The fields should be `Long` because `a << b` with mess with use if `b` >= 32
   * Doesn't work with negative numbers
   */
  def apply(timestamp: Long, generatorNr: Long, sequenceNr: Long, counter: Long, randomSeed: Long): ID = {
    val timestampBits = (timestamp & timestampMask) << timestampShift
    val generatorNrBits = (generatorNr & generatorNrMask) << generatorNrShift
    val counterBits = (counter & counterMask) << counterShift
    val sequenceNrBits = (sequenceNr & sequenceNrMask) << sequenceNrShift
    val randomSeedBits = (randomSeed & randomSeedMask) << randomSeedShift
    ID(timestampBits | generatorNrBits | counterBits, sequenceNrBits | randomSeedBits)
  }

  def apply(uuid: UUID): ID = ID(uuid.getMostSignificantBits, uuid.getLeastSignificantBits)

  def apply(base64: String): ID = {
    val binary = encoder.decode(base64)
    require(binary.length == 8 + 8, s"fromBinary($base64) - length ${binary.length}")
    val bb = ByteBuffer.wrap(binary)
    val msb = bb.getLong()
    val lsb = bb.getLong()
    ID(msb, lsb)
  }

  /** Limits the number of ID generated per message to 4096 */
  def counterSize: Int = 12
  def counterShift: Int = 0
  def counterMask: Long = mask(counterSize)

  /** Limits the number of generator types to 1024 */
  def generatorNrSize: Int = 10
  def generatorNrShift: Int = counterShift + counterSize
  def generatorNrMask: Long = mask(generatorNrSize)

  /** Limits the time of roll-over to about 139 years */
  def timestampSize: Int = 42
  def timestampShift: Int = generatorNrShift + generatorNrSize
  def timestampMask: Long = mask(timestampSize)

  /** Probability of 2 Actors having same seed is less than 1 / 4 billions */
  def randomSeedSize: Int = 32
  def randomSeedShift: Int = 0
  def randomSeedMask: Long = mask(randomSeedSize)

  /** Limits the number of messages received per Actor to 1024 * 1024 */
  def sequenceNrSize: Int = 20
  def sequenceNrShift: Int = randomSeedShift + randomSeedSize
  def sequenceNrMask: Long = mask(sequenceNrSize)

  private def mask(size: Int): Long = -1L ^ (-1L << size)
}
