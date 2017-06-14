package org.openurp.platform.config.service.impl

import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.openurp.platform.config.model.Db
import org.openurp.platform.config.service.DbService

class DbServiceImpl(entityDao: EntityDao) extends DbService {

  override def list(): Seq[Db] = {
    val query = OqlBuilder.from(classOf[Db], "o")
    entityDao.search[Db](query)
  }

}