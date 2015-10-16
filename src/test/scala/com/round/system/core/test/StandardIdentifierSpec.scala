package com.round.system.core.test

import org.specs2.mutable.Specification

import com.round.system.core.StandardIdentifier

class StandardIdentifierSpec extends Specification {

  "A StandardIdentifier" should {
    "should be created correctly from datacenter id, generator id, sequence number and counter" in {
      val did = 3L
      val gid = 4L
      val snr = 435L
      val ctr = 3L
      val id = StandardIdentifier(did, gid, snr, ctr)

      id.datacenterId must_== did
      id.generatorId must_== gid
      id.sequenceNr must_== snr
      id.counter must_== ctr
    }

    "should be created correctly from most significant bits, and least significant bits" in {
      val msb = 3L
      val lsb = 4L
      val id = StandardIdentifier(msb, lsb)

      id.msb must_== msb
      id.lsb must_== lsb
    }

    "should be created the same with most significant bits, and least significant bits" in {
      val did = 3L
      val gid = 4L
      val snr = 435L
      val ctr = 3L
      val sid = StandardIdentifier(did, gid, snr, ctr)
      val id = StandardIdentifier(sid.msb, sid.lsb)

      id.datacenterId must_== did
      id.generatorId must_== gid
      id.sequenceNr must_== snr
      id.counter must_== ctr
    }

    "should be created the same with datacenter id, generator id, sequence number and counter" in {
      val msb = 3L
      val lsb = 4L
      val sid = StandardIdentifier(msb, lsb)
      val id = StandardIdentifier(sid.datacenterId, sid.generatorId, sid.sequenceNr, sid.counter)

      id.msb must_== msb
      id.lsb must_== lsb
    }

    "should be created the same from java UUID" in {
      val msb = 3L
      val lsb = 4L
      val id = StandardIdentifier(msb, lsb)
      val uuid = id.uuid

      id must_== StandardIdentifier(uuid)
    }

    "should be created the same from base64" in {
      val msb = 3453L
      val lsb = 544L
      val id = StandardIdentifier(msb, lsb)
      val base64 = id.base64

      id must_== StandardIdentifier(base64)
    }
  }
}
