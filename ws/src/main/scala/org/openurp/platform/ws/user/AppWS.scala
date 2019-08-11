/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.platform.ws.user

import org.beangle.commons.collection.{Collections, Properties}
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.webmvc.api.action.{ActionSupport, EntitySupport}
import org.beangle.webmvc.api.annotation.{mapping, param, response}
import org.openurp.platform.config.model.{App, AppType}
import org.openurp.platform.security.model.FuncPermission
import org.openurp.platform.user.model.{Root, User}
import org.openurp.platform.user.service.UserService

/**
 * @author chaostone
 */
class AppWS(userService: UserService, entityDao: EntityDao) extends ActionSupport with EntitySupport[User] {

  @response
  @mapping("{userCode}")
  def index(@param("userCode") userCode: String): collection.Seq[Properties] = {
    userService.get(userCode) match {
      case Some(user) =>
        val fpAppQuery = OqlBuilder.from[App](classOf[FuncPermission].getName, "fp")
          .join("fp.role.members", "m")
          .where("m.user=:user and m.member=true", user)
          .where("fp.resource.app.enabled=true")
          .where(s"fp.resource.app.appType.name='${AppType.Webapp}'")
          .select("distinct fp.resource.app").cacheable()

        val fpApps = entityDao.search(fpAppQuery)

        val apps = Collections.newSet[App]
        apps ++= fpApps

        val rootsQuery = OqlBuilder.from(classOf[Root], "root")
          .where(s"root.user=:user and root.app.enabled=true and root.app.appType.name='${AppType.Webapp}'", user)
          .cacheable()
        val roots = entityDao.search(rootsQuery)
        apps ++= roots.map(a => a.app)
        val domain = get("domain")
        domain foreach { d =>
          apps --= apps.filter { a => a.domain == null || a.domain.name != d }
        }
        val appBuffer = apps.toBuffer.sorted
        appBuffer.map { app =>
          val p = new Properties(app, "id", "name", "title", "base", "url", "logoUrl", "navStyle")
          p.add("domain", app.domain, "id", "name", "title")
          p
        }
      case None => Seq.empty
    }
  }
}
