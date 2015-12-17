package org.openurp.platform.user.service

import org.openurp.platform.user.model.Dimension

trait DataProvider {
  def getData[T](field: Dimension, source: String, keys: Any*): Seq[T]
}
