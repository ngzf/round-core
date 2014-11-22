package com.zitadelz.system.core

object TestIdentifier {
  def apply[A <: HasId[A]]: Identifier[A] = TestIdentifier[A](0, 0)
}

case class TestIdentifier[A <: HasId[A]](msb: Long, lsb: Long) extends Identifier[A] {
  override def datacenterId: Long = -1
  override def generatorId: Long = -1
  override def sequenceNr: Long = lsb
  override def counter: Long = -1
  override lazy val base64: String = s"TestIdentifierBase64($msb, $lsb)"
}
