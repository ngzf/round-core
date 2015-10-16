package com.round.system.core

import java.util.UUID

/**
 * Universally unique identifiers.
 */
trait Identifier[A <: HasId[A]] extends HasId[A] {
  override final val id = this
  final val uuid = new UUID(msb, lsb)

  def msb: Long
  def lsb: Long
  def datacenterId: Long
  def generatorId: Long
  def sequenceNr: Long
  def counter: Long
  def base64: String
}

private[core] object Identifier {

  def apply[A <: HasId[A]](msb: Long, lsb: Long): Identifier[A] =
    StandardIdentifier(msb, lsb)

  def apply[A <: HasId[A]](datacenterId: Long, generatorId: Long, sequenceNr: Long, counter: Long): StandardIdentifier[A] =
    StandardIdentifier(datacenterId, generatorId, sequenceNr, counter)

  def apply[A <: HasId[A]](uuid: UUID): StandardIdentifier[A] =
    StandardIdentifier(uuid.getMostSignificantBits, uuid.getLeastSignificantBits)

  def apply[A <: HasId[A]](base64: String): StandardIdentifier[A] =
    StandardIdentifier(base64)
}
