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
      val id = StandardIdentifier(did, gid, snr, ctr, Vector.empty)

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
      val sid = StandardIdentifier(did, gid, snr, ctr, Vector.empty)
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
      val id = StandardIdentifier(sid.datacenterId, sid.generatorId, sid.sequenceNr, sid.counter, Vector.empty)

      id.msb must_== msb
      id.lsb must_== lsb
    }

    "should be created the same from java UUID" in {
      val msb = 3L
      val lsb = 4L
      val id = StandardIdentifier(msb, lsb)
      val uuid = id.uuid

      id must_== StandardIdentifier(uuid, Vector.empty)
    }

    "should be created the same from base64" in {
      val msb = 3453L
      val lsb = 544L
      val id = StandardIdentifier(msb, lsb)
      val base64 = id.base64

      id must_== StandardIdentifier(base64)
    }

    "be recovered from a base64" in {
      "without sharding part" in {
        val id = StandardIdentifier(1337l, 14l)
        id must_== StandardIdentifier(id.base64)
      }

      "with sharding part" in {
        "cannot be converted to UUID" in {
          try {
            val uuid = StandardIdentifier(1337l, 14l, Vector((2L, 1), (3L, 3), (4L, 5))).uuid
            uuid must_!= uuid
          } catch {
            case e: RuntimeException => 1 must_== 1
          }
        }
        "base64" in {
          val id = StandardIdentifier(1337l, 14l, Vector((2L, 1), (3L, 3), (4L, 5)))
          id must_== StandardIdentifier(id.base64)
        }
      }
    }

    "correctly implement extra copy method" in {
      val ref = StandardIdentifier(java.util.UUID.randomUUID(), Vector.empty)
      ref.copy(datacenterId = 0) must_== StandardIdentifier(0, ref.generatorId, ref.sequenceNr, ref.counter, Vector.empty)
      ref.copy(datacenterId = 1) must_== StandardIdentifier(1, ref.generatorId, ref.sequenceNr, ref.counter, Vector.empty)
      ref.copy(generatorId = 0) must_== StandardIdentifier(ref.datacenterId, 0, ref.sequenceNr, ref.counter, Vector.empty)
      ref.copy(generatorId = 1) must_== StandardIdentifier(ref.datacenterId, 1, ref.sequenceNr, ref.counter, Vector.empty)
      ref.copy(counter = 0) must_== StandardIdentifier(ref.datacenterId, ref.generatorId, ref.sequenceNr, 0, Vector.empty)
      ref.copy(counter = 1) must_== StandardIdentifier(ref.datacenterId, ref.generatorId, ref.sequenceNr, 1, Vector.empty)
      ref.copy(sequenceNr = 0) must_== StandardIdentifier(ref.datacenterId, ref.generatorId, 0, ref.counter, Vector.empty)
      ref.copy(sequenceNr = 1) must_== StandardIdentifier(ref.datacenterId, ref.generatorId, 1, ref.counter, Vector.empty)
      ref.copy(counters = Vector((2L, 1), (3L, 3), (4L, 5))) must_== StandardIdentifier(ref.datacenterId, ref.generatorId, ref.sequenceNr, ref.counter, Vector((2L, 1), (3L, 3), (4L, 5)))
    }

    "correctly generate new ID hierarchically" in {
      "empty counters" in {
        val id = StandardIdentifier(324l, 134l)
        val x = id.nextLevel(1, 2)
        x must_== StandardIdentifier(324l, 134l, Vector(1L -> 2))
      }
      "non-empty counters" in {
        val id = StandardIdentifier(324l, 134l, Vector(1L -> 4, 4L -> 3))
        id.nextLevel(1, 2) must_== StandardIdentifier(324l, 134l, Vector(1L -> 4, 4L -> 3, 1L -> 2))
      }
      "does not hurt the base ID" in {
        val id = StandardIdentifier(324l, 134l, Vector(1L -> 4, 4L -> 3))
        id.nextLevel(1, 2)
        id must_== StandardIdentifier(324l, 134l, Vector(1L -> 4, 4L -> 3))
      }
    }
  }
}
