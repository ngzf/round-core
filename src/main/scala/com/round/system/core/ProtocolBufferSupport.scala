package com.round.system.core

import language.higherKinds
import language.implicitConversions

import scala.collection.TraversableLike
import scala.collection.generic.CanBuildFrom

import com.trueaccord.scalapb.TypeMapper

trait ProtocolBufferSupport {
  @inline
  implicit def toProtuf[I, P <: Protuf[P]](id: I)(implicit m: TypeMapper[I, P]): P =
    m.toCustom(id)

  @inline
  implicit def fromProtuf[I, P <: Protuf[P]](protuf: P)(implicit m: TypeMapper[I, P]): I =
    m.toBase(protuf)

  @inline
  implicit def toProtufs[I, P <: Protuf[P], C[A] <: TraversableLike[A, C[A]]](ids: C[I])(implicit m: TypeMapper[I, P], cbf: CanBuildFrom[C[I], P, C[P]]): C[P] =
    ids.map(m.toCustom)

  @inline
  implicit def fromProtufs[I, P <: Protuf[P], C[A] <: TraversableLike[A, C[A]]](ids: C[P])(implicit m: TypeMapper[I, P], cbf: CanBuildFrom[C[P], I, C[I]]): C[I] =
    ids.map(m.toBase)
}
