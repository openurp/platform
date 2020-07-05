package org.openurp.platform.config.service.impl

import org.beangle.commons.bean.Initializing
import org.beangle.data.dao.EntityDao
import org.openurp.app.Urp
import org.openurp.platform.config.model.Domain

class DomainServiceImpl extends DomainService with Initializing {
  var entityDao: EntityDao = _

  var domain: Domain = _

  override def getDomain: Domain = {
    domain
  }

  override def init(): Unit = {
    val rs = entityDao.findBy(classOf[Domain], "hostname", List(Urp.hostname))
    rs.headOption match {
      case Some(d) => domain = d
      case None => throw new RuntimeException("Cannot find domain with hostname :" + Urp.hostname)
    }
  }
}
