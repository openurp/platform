/*
 * Beangle, Agile Development Scaffold and Toolkit
 *
 * Copyright (c) 2005-2017, Beangle Software.
 *
 * Beangle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beangle is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Beangle.  If not, see <http://www.gnu.org/licenses/>.
 */
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
    val query = OqlBuilder.from(classOf[App], "app")
      .where("app.name=:name and app.secret=:secret", name, secret).cacheable()
    val apps = entityDao.search(query)
    initialize(apps)
    if (apps.isEmpty) None else Some(apps.head)
  }

  override def getApp(name: String): Option[App] = {
    val query = OqlBuilder.from(classOf[App], "app").where("app.name=:name ", name).cacheable()
    val apps = entityDao.search(query)
    initialize(apps)
    if (apps.isEmpty) None else Some(apps.head)
  }

  private def initialize(apps: Iterable[App]): Unit = {
    apps foreach { app =>
      app.domain.title
    }
  }

  override def getWebapps(): Seq[App] = {
    entityDao.search(OqlBuilder.from(classOf[App], "app").where("app.appType=:typ and app.enabled=true", "web-app").orderBy("app.indexno"))
  }

  override def getApps(): Seq[App] = {
    entityDao.search(OqlBuilder.from(classOf[App], "app").where("app.enabled=true").orderBy("app.indexno"))
  }
}
