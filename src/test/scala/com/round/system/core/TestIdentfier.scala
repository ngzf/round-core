package com.round.system.core

object TestIdentifier {
  def apply[A <: HasId[A]]: Identifier[A] = TestIdentifier[A](0, 0)
}

case class TestIdentifier[A <: HasId[A]](msb: Long, lsb: Long, counters: Vector[Identifier.Counter] = Vector.empty) extends Identifier[A] {
  override def datacenterId: Long = -1
  override def generatorId: Long = -1
  override def sequenceNr: Long = lsb
  override def counter: Long = -1
  override def nextLevel[B <: HasId[B]](sequenceNr: Long, counter: Int): Identifier[B] =
    TestIdentifier(msb, lsb, counters :+ Identifier.Counter(sequenceNr, counter))
  override lazy val base64: String = s"TestIdentifierBase64($msb, $lsb, $counters)"
}
