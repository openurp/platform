package org.openurp.resource.service

import org.openurp.resource.model.DbBean

trait DbService {

  def list(): Seq[DbBean]

}