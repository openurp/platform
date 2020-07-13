package org.openurp.platform.user.service.impl

import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.openurp.platform.config.service.DomainService
import org.openurp.platform.user.model.Dimension
import org.openurp.platform.user.service.DimensionService

class DimensionServiceImpl extends DimensionService {

  var domainService: DomainService = _
  var entityDao: EntityDao = _

  override def getAll(): Seq[Dimension] = {
    val query = OqlBuilder.from(classOf[Dimension], "d")
      .where("d.domain=:domain", domainService.getDomain)
      .cacheable()
    entityDao.search(query)
  }

  override def get(name: String): Option[Dimension] = {
    val query = OqlBuilder.from(classOf[Dimension], "d")
      .where("d.name=:name", name)
      .where("d.domain=:domain", domainService.getDomain)
      .cacheable()
    entityDao.search(query).headOption
  }
}
