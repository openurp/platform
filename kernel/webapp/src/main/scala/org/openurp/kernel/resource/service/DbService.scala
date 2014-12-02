package org.openurp.kernel.resource.service

import org.openurp.kernel.resource.model.DbBean

trait DbService {

  def list(): Seq[DbBean]

}