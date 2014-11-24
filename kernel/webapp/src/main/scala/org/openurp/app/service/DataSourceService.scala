package org.openurp.app.service

import org.openurp.app.resource.model.DataSourceCfgBean

trait DataSourceService {

  def findDataSource(): Seq[DataSourceCfgBean]

}