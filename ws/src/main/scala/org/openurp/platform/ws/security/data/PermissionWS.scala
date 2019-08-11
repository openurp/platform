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
package org.openurp.platform.ws.security.data

import org.beangle.commons.collection.Properties
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.{mapping, param, response}
import org.openurp.platform.config.model.App
import org.openurp.platform.security.model.DataPermission
import org.openurp.platform.user.model.User

/**
 * @author chaostone
 */
class PermissionWS(entityDao: EntityDao) extends ActionSupport {

  @response
  @mapping("user/{userCode}")
  def index(@param("app") appName: String, @param("userCode") userCode: String, @param("data") dataName: String): Any = {
    val userQuery = OqlBuilder.from(classOf[User], "u").where("u.code =:userCode", userCode)
    val users = entityDao.search(userQuery)
    val apps = entityDao.findBy(classOf[App], "name", List(appName))
    if (users.isEmpty || apps.isEmpty) {
      List.empty
    } else {
      val u = users.head
      val app = apps.head

      val roleSet = u.roles.filter(r => r.member).map(r => r.role).toSet
      val permissionQuery = OqlBuilder.from(classOf[DataPermission], "dp")
      permissionQuery.where("dp.domain=:domain and dp.resource.name=:dataName", app.domain, dataName)
        .cacheable(true)
      val permissions = entityDao.search(permissionQuery)
      val mostFavorates = permissions find (p => p.app.isDefined && p.role.isDefined && roleSet.contains(p.role.get))
      val p = mostFavorates match {
        case Some(p) => p
        case None =>
          permissions find (x => x.app.isDefined && x.role.isEmpty) match {
            case Some(p) => p
            case None =>
              permissions find (x => x.app.isEmpty && x.role.isDefined) match {
                case Some(p) => p
                case None =>
                  val pp = permissions find (x => x.app.isEmpty && x.role.isEmpty)
                  pp.orNull
              }
          }
      }
      val props = new Properties()
      if (p != null) props.put("filters", p.filters)
      props
    }
  }
}
