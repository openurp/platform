package org.openurp.platform.config.service

import org.openurp.platform.config.model.Db

trait DbService {

  def list(): Seq[Db]

}