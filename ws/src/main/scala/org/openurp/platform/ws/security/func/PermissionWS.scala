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
package org.openurp.platform.ws.security.func

import org.beangle.commons.collection.Properties
import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.{ mapping, param, response }
import org.openurp.platform.security.model.{ FuncPermission, FuncResource }
import org.openurp.platform.security.model.FuncPermission

/**
 * @author chaostone
 */
class PermissionWS(entityDao: EntityDao) extends ActionSupport {

  @response
  @mapping("role/{roleId}")
  def role(@param("app") app: String, @param("roleId") roleId: Int): Any = {
    val roleQuery = OqlBuilder.from[FuncResource](classOf[FuncPermission].getName, "fp")
      .where("fp.resource.app.name = :appName", app).where("fp.role.id = :roleId", roleId)
      .cacheable()
      .select("fp.resource")
    val resources = entityDao.search(roleQuery)
    resources.map { r => new Properties(r, "id", "name", "title", "scope") }
  }
}
