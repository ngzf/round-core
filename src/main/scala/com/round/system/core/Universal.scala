package com.round.system.core

/**
 * Model data objects that can be used all around the Zitadels systems, backend
 * and/or frontend. In general, the `Universal` objects are composite and not
 * suitable to be used at the persistent layer. For such a purpose, use their
 * [[Persistent[U]]] counterpart.
 */
trait Universal

/**
 * Counterpart of the model object `U` at the persistence layer.
 */
trait Persistent[U <: Universal] {
  def universal: U
}
