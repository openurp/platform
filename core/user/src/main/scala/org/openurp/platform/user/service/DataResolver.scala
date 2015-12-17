package org.openurp.platform.user.service

import org.openurp.platform.user.model.Dimension

trait DataResolver {
  def marshal(field: Dimension, items: Seq[Any]): String
  def unmarshal[T](field: Dimension, text: String): Seq[T]
}
