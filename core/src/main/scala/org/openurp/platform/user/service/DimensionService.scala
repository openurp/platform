package org.openurp.platform.user.service

import org.openurp.platform.user.model.Dimension

trait DimensionService {

  def getAll(): Seq[Dimension]

  def get(name: String): Option[Dimension]
}
