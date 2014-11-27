package org.openurp.resource.service

import org.beangle.data.jpa.dao.OqlBuilder
import org.beangle.data.model.dao.EntityDao
import org.openurp.resource.model.DbBean

class DbServiceImpl(entityDao: EntityDao) extends DbService {

  override def list(): Seq[DbBean] = {
    val query = OqlBuilder.from(classOf[DbBean], "o")
    entityDao.search[DbBean](query)
  }

}