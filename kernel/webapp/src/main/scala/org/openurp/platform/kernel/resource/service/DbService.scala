package org.openurp.platform.kernel.resource.service

import org.openurp.platform.kernel.resource.model.Db

trait DbService {

  def list(): Seq[Db]

}