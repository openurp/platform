package org.openurp.platform.kernel.resource.service

import org.beangle.data.jpa.dao.OqlBuilder
import org.beangle.data.model.dao.EntityDao
import org.openurp.platform.kernel.resource.model.Db

class DbServiceImpl(entityDao: EntityDao) extends DbService {

  override def list(): Seq[Db] = {
    val query = OqlBuilder.from(classOf[Db], "o")
    entityDao.search[Db](query)
  }

}