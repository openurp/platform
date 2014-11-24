package org.openurp.app.service

import org.beangle.data.jpa.dao.OqlBuilder
import org.beangle.data.model.dao.EntityDao
import org.openurp.app.resource.model.DataSourceCfgBean

class DataSourceServiceImpl(entityDao: EntityDao) extends DataSourceService {

  def findDataSource(): Seq[DataSourceCfgBean] = {
    val query = OqlBuilder.from(classOf[DataSourceCfgBean], "o")
    entityDao.search[DataSourceCfgBean](query)
  }

}