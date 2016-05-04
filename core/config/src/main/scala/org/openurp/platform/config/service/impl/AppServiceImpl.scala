package org.openurp.platform.config.service.impl

import org.openurp.platform.config.service.AppService
import org.beangle.data.dao.EntityDao
import org.beangle.data.dao.OqlBuilder
import org.openurp.platform.config.model.App

/**
 * @author chaostone
 */
class AppServiceImpl(entityDao: EntityDao) extends AppService {

  override def getApp(name: String, secret: String): Option[App] = {
    val query = OqlBuilder.from(classOf[App], "app").where("app.name=:name and app.secret=:secret", name, secret).cacheable()
    val apps = entityDao.search(query)
    if (apps.isEmpty) None else Some(apps.head)
  }

  override def getWebapps(): Seq[App] = {
    entityDao.search(OqlBuilder.from(classOf[App], "app").where("app.appType=:typ and app.enabled=true", "web-app").orderBy("app.indexno"))
  }

  override def getApps(): Seq[App] = {
    entityDao.search(OqlBuilder.from(classOf[App], "app").where("app.enabled=true").orderBy("app.indexno"))
  }
}
