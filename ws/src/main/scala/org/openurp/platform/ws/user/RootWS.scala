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

import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.webmvc.api.action.{ ActionSupport, EntitySupport }
import org.beangle.webmvc.api.annotation.{ param, response }
import org.openurp.platform.user.model.User
import org.openurp.platform.user.service.UserService
import org.openurp.platform.user.model.Root
import org.openurp.platform.user.model.Root

/**
 * @author chaostone
 */
class RootWS(userService: UserService, entityDao: EntityDao) extends ActionSupport with EntitySupport[User] {

  @response
  def index(@param("app") app: String): Seq[String] = {
    val query = OqlBuilder.from[String](classOf[Root].getName, "r")
    query.where("r.app.name = :appName", app).select("r.user.code")
    .cacheable()
    entityDao.search(query)
  }
}
