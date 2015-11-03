package org.openurp.platform.kernel.service.impl

import org.openurp.platform.kernel.service.AppService
import org.beangle.data.dao.EntityDao
import org.beangle.data.dao.OqlBuilder
import org.openurp.platform.kernel.model.App

/**
 * @author chaostone
 */
class AppServiceImpl(entityDao: EntityDao) extends AppService {

  override def getApp(name: String, secret: String): Option[App] = {
    val query = OqlBuilder.from(classOf[App], "app").where("app.name=:name and app.secret=:secret", name, secret).cacheable()
    val apps = entityDao.search(query)
    if (apps.isEmpty) None else Some(apps.head)
  }
}