package com.round
package test

import org.specs2.mutable.Specification

class IDSpec extends Specification {

  s"An ${classOf[ID].getSimpleName}" should {
    "should be created correctly from timestamp, datatype number, sequence number, counter, and random seed" in {
      val ts = 3L
      val gid = 4L
      val snr = 435L
      val ctr = 3L
      val rd = 12L
      val id = ID(ts, gid, snr, ctr, rd)
      id.timestamp must_== ts
      id.datatypeNr must_== gid
      id.sequenceNr must_== snr
      id.counter must_== ctr
      id.randomSeed must_== rd
    }

    "should be created correctly from most significant bits, and least significant bits" in {
      val msb = 3L
      val lsb = 4L
      val id = ID(msb, lsb)

      id.msb must_== msb
      id.lsb must_== lsb
    }

    "should be created the same with most significant bits, and least significant bits" in {
      val ts = 3L
      val gid = 4L
      val snr = 435L
      val ctr = 3L
      val rd = 7
      val sid = ID(ts, gid, snr, ctr, 7)
      val id = ID(sid.msb, sid.lsb)
      id.timestamp must_== ts
      id.datatypeNr must_== gid
      id.sequenceNr must_== snr
      id.counter must_== ctr
      id.randomSeed must_== rd
    }

    "should be created the same with timestamp, datatype number, sequence number, counter, and random seed" in {
      val msb = 3L
      val lsb = 4L
      val sid = ID(msb, lsb)
      val id = ID(sid.timestamp, sid.datatypeNr, sid.sequenceNr, sid.counter, sid.randomSeed)

      id.msb must_== msb
      id.lsb must_== lsb
    }

    "should be created the same from java UUID" in {
      val msb = 3L
      val lsb = 4L
      val id = ID(msb, lsb)
      val uuid = id.uuid

      id must_== ID(uuid)
    }

    "should be created the same from base64" in {
      val msb = 3453L
      val lsb = 544L
      val id = ID(msb, lsb)
      val base64 = id.base64

      id must_== ID(base64)
    }

    "be recovered from a base64" in {
      val id = ID(1337l, 14l)
      id must_== ID(id.base64)
    }

    "correctly implement extra copy method" in {
      val ref = ID(java.util.UUID.randomUUID())
      println(ref)
      ref.copy(timestamp = 0) must_== ID(0, ref.datatypeNr, ref.sequenceNr, ref.counter, ref.randomSeed)
      ref.copy(timestamp = 1) must_== ID(1, ref.datatypeNr, ref.sequenceNr, ref.counter, ref.randomSeed)
      ref.copy(datatypeNr = 0) must_== ID(ref.timestamp, 0, ref.sequenceNr, ref.counter, ref.randomSeed)
      ref.copy(datatypeNr = 1) must_== ID(ref.timestamp, 1, ref.sequenceNr, ref.counter, ref.randomSeed)
      ref.copy(counter = 0) must_== ID(ref.timestamp, ref.datatypeNr, ref.sequenceNr, 0, ref.randomSeed)
      ref.copy(counter = 1) must_== ID(ref.timestamp, ref.datatypeNr, ref.sequenceNr, 1, ref.randomSeed)
      ref.copy(sequenceNr = 0) must_== ID(ref.timestamp, ref.datatypeNr, 0, ref.counter, ref.randomSeed)
      ref.copy(sequenceNr = 1) must_== ID(ref.timestamp, ref.datatypeNr, 1, ref.counter, ref.randomSeed)
      ref.copy(randomSeed = 0) must_== ID(ref.timestamp, ref.datatypeNr, ref.sequenceNr, ref.counter, 0)
      ref.copy(randomSeed = 1) must_== ID(ref.timestamp, ref.datatypeNr, ref.sequenceNr, ref.counter, 1)
    }
  }
}
