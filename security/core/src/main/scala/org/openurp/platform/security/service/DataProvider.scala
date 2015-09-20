package org.openurp.platform.security.service

import org.openurp.platform.security.model.Dimension

trait DataProvider {
  def getData[T](field: Dimension, source: String, keys: Any*): Seq[T]
}
