package org.openurp.platform.security.service

import org.openurp.platform.security.model.Dimension

trait DataResolver {
  def marshal(field: Dimension, items: Seq[Any]): String
  def unmarshal[T](field: Dimension, text: String): Seq[T]
}
