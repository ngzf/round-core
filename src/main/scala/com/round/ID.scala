package com.round

import java.nio.ByteBuffer
import java.util.UUID

import org.apache.commons.codec.binary.Base64

import play.api.libs.json._

import com.trueaccord.scalapb.TypeMapper

case class ID(msb: Long, lsb: Long) {
  lazy val uuid = new UUID(msb, lsb)
  lazy val binary = ByteBuffer.allocate(8 + 8).putLong(msb).putLong(lsb).array()
  lazy val base64 = ID.encoder.encodeAsString(binary)

  def timestamp: Long = msb >> ID.timestampShift & ID.timestampMask
  def datatypeNr: Long = msb >> ID.datatypeNrShift & ID.datatypeNrMask
  def counter: Long = msb >> ID.counterShift & ID.counterMask
  def reserved: Long = lsb >> ID.reservedShift & ID.reservedMask
  def sequenceNr: Long = lsb >> ID.sequenceNrShift & ID.sequenceNrMask
  def randomSeed: Long = lsb >> ID.randomSeedShift & ID.randomSeedMask

  def copy(timestamp: Long = timestamp, datatypeNr: Long = datatypeNr, sequenceNr: Long = sequenceNr, counter: Long = counter, randomSeed: Long = randomSeed): ID = {
    val base = ID(timestamp, datatypeNr, sequenceNr, counter, randomSeed)
    val reservedBits = (this.reserved & ID.reservedMask) << ID.reservedShift
    ID(msb = base.msb, lsb = base.lsb | reservedBits)
  }

  def reserved(reservedType: ID.Reserved.ReservedType): ID =
    ID(msb = msb, lsb = lsb | reservedType.reservedBits)

  override def toString(): String = s"ID($base64|$timestamp|$datatypeNr|$sequenceNr|$counter|$randomSeed|$reserved)"
}

/**
 * | 128 bits of an ID                                                     |
 * | most significant 64 bits         | least significant 64 bits          |
 * | timestamp | datatypeNr | counter | reserved | sequenceNr | randomSeed |
 * | 42 bits   | 10 bits    | 12 bits | 12 bits  | 20 bits    | 32 bits    |
 */
object ID {

  object Reserved {
    abstract class ReservedType(reserved: Int) {
      val reservedBits = (reserved & reservedMask) << reservedShift
    }
    object Bootstrap extends ReservedType(1)
  }

  val encoder = new Base64(-1, Array.empty[Byte], true)

  implicit val typeMapper = TypeMapper[String, ID](apply)(_.base64)

  implicit val jsonFormat = new Format[ID] {
    override def reads(json: JsValue): JsResult[ID] = JsSuccess(ID(json.as[String]))
    override def writes(i: ID): JsString = JsString(i.base64)
  }

  /**
   * The fields should be `Long` because `a << b` with mess with use if `b` >= 32
   * Doesn't work with negative numbers
   */
  def apply(timestamp: Long, datatypeNr: Long, sequenceNr: Long, counter: Long, randomSeed: Long): ID = {
    val timestampBits = (timestamp & timestampMask) << timestampShift
    val datatypeNrBits = (datatypeNr & datatypeNrMask) << datatypeNrShift
    val counterBits = (counter & counterMask) << counterShift
    val reservedBits = (0 & reservedMask) << reservedShift
    val sequenceNrBits = (sequenceNr & sequenceNrMask) << sequenceNrShift
    val randomSeedBits = (randomSeed & randomSeedMask) << randomSeedShift
    ID(timestampBits | datatypeNrBits | counterBits, reservedBits | sequenceNrBits | randomSeedBits)
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

  /** Limits the number of datatype types to 1024 */
  def datatypeNrSize: Int = 10
  def datatypeNrShift: Int = counterShift + counterSize
  def datatypeNrMask: Long = mask(datatypeNrSize)

  /** Limits the time of roll-over to about 139 years */
  def timestampSize: Int = 42
  def timestampShift: Int = datatypeNrShift + datatypeNrSize
  def timestampMask: Long = mask(timestampSize)

  /** Probability of 2 Actors having same seed is less than 1 / 4 billions */
  def randomSeedSize: Int = 32
  def randomSeedShift: Int = 0
  def randomSeedMask: Long = mask(randomSeedSize)

  /** Limits the number of messages received per Actor to 1024 * 1024 */
  def sequenceNrSize: Int = 20
  def sequenceNrShift: Int = randomSeedShift + randomSeedSize
  def sequenceNrMask: Long = mask(sequenceNrSize)

  def reservedSize: Int = 12
  def reservedShift: Int = sequenceNrShift + sequenceNrSize
  def reservedMask: Long = mask(reservedSize)

  private def mask(size: Int): Long = -1L ^ (-1L << size)
}
