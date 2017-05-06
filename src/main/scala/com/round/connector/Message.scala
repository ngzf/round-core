package com.round
package connector

trait Message {
  def timestamp: Long
}

trait Event extends Message {
  def sequenceNr: Int
  def randomSeed: Long
}
