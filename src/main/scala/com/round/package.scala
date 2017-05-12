package com

import scalaz.syntax.ToNelOps
import scalaz.syntax.ToValidationOps
import scalaz.syntax.std.ToOptionIdOps

package object round
  extends Alias
  with TimeMapper
  with ToValidationOps
  with ToNelOps
  with ToOptionIdOps
