package com.round
package connector

trait Message {
  def timestamp: Long
}

object Message {
  def unapply(m: Message): Option[Long] = m.timestamp.some
}

trait Event extends Message {
  def sequenceNr: Option[Int]
  def randomSeed: Option[Long]
}

object Event {
  def unapply(e: Event): Option[Option[Int] -> Option[Long]] = (e.sequenceNr, e.randomSeed).some
}

trait WithUser {
  def userID: Option[ID]
}

object WithUser {
  def unapply(u: WithUser): Option[Option[ID]] = (u.userID).some
}
