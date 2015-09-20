package org.openurp.platform.kernel.service

import org.openurp.platform.kernel.model.Db

trait DbService {

  def list(): Seq[Db]

}