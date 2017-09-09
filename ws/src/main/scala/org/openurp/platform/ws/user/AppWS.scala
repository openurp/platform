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
package org.openurp.platform.ws.user

import org.beangle.commons.collection.Properties
import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.webmvc.api.action.{ ActionSupport, EntitySupport }
import org.beangle.webmvc.api.annotation.{ param, response }
import org.openurp.platform.config.model.App
import org.openurp.platform.user.model.User
import org.openurp.platform.user.service.UserService
import org.openurp.platform.security.model.FuncPermission
import org.openurp.platform.user.model.Root
import org.beangle.webmvc.api.annotation.mapping
import org.beangle.commons.collection.Collections
import org.openurp.platform.security.model.FuncPermission
import org.openurp.platform.user.model.Root
import org.openurp.platform.config.model.AppType

/**
 * @author chaostone
 */
class AppWS(userService: UserService, entityDao: EntityDao) extends ActionSupport with EntitySupport[User] {

  @response
  @mapping("{userCode}")
  def index(@param("userCode") userCode: String): Seq[Properties] = {
    userService.get(userCode) match {
      case Some(user) =>
        val fpAppQuery = OqlBuilder.from[App](classOf[FuncPermission].getName, "fp")
          .join("fp.role.members", "m")
          .where("m.user=:user and m.member=true", user)
          .where("fp.resource.app.enabled=true")
          .where(s"fp.resource.app.appType='${AppType.Webapp}'")
          .select("distinct fp.resource.app").cacheable()

        val fpApps = entityDao.search(fpAppQuery)

        val apps = Collections.newSet[App]
        apps ++= fpApps

        val rootsQuery = OqlBuilder.from(classOf[Root], "root")
          .where(s"root.user=:user and root.app.enabled=true and root.app.appType='${AppType.Webapp}'", user)
          .cacheable()
        val roots = entityDao.search(rootsQuery)
        apps ++= (roots.map(a => a.app))
        val domain = get("domain")
        domain foreach { d =>
          apps --= apps.filter { a => a.domain == null || a.domain.name != d }
        }
        val appBuffer = apps.toBuffer.sorted
        appBuffer.map(app => new Properties(app, "id", "name", "title", "url", "logoUrl"))
      case None => Seq.empty
    }
  }
}
