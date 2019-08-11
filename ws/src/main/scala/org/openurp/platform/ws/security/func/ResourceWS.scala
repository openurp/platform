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
package org.openurp.platform.ws.security.func

import org.beangle.commons.collection.{Collections, Properties}
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.security.authz.Scopes
import org.beangle.webmvc.api.action.{ActionSupport, EntitySupport}
import org.beangle.webmvc.api.annotation.{mapping, param, response}
import org.openurp.platform.security.model.{FuncPermission, FuncResource}

/**
 * 系统功能资源web服务
 */
class ResourceWS(entityDao: EntityDao) extends ActionSupport with EntitySupport[FuncResource] {

  @response
  def index(@param("app") app: String): Seq[Any] = {
    val query = OqlBuilder.from(classOf[FuncResource], "fr").where("fr.app.name=:name", app)
    get("scope") match {
      case Some("Private")   => query.where("fr.scope = :scope", Scopes.Private)
      case Some("Protected") => query.where("fr.scope = :scope", Scopes.Protected)
      case Some("Public")    => query.where("fr.scope = :scope", Scopes.Public)
      case _                 =>
    }
    val resources = entityDao.search(query)
    val permissionQuery = OqlBuilder.from[Array[Object]](classOf[FuncPermission].getName, "fp")
      .where("fp.resource.app.name = :appName", app)
      .select("fp.resource.id,fp.role.id")
      .cacheable()

    val permissions = Collections.newMap[Number, collection.mutable.Set[Number]]
    entityDao.search(permissionQuery) foreach { p =>
      permissions.getOrElseUpdate(p(0).asInstanceOf[Number], new collection.mutable.HashSet[Number]) += p(1).asInstanceOf[Number]
    }

    resources map { r =>
      val p = new Properties(r, "id", "name", "title", "scope")
      p.put("roles", permissions.getOrElse(r.id, Set.empty).toArray)
      p
    }
  }

  @response
  def info(@param("app") app: String, @param("name") name: String): Properties = {
    val query = OqlBuilder.from(classOf[FuncResource], "fr").where("fr.app.name=:app", app)
    query.where("fr.name=:name", name).cacheable()
    val resources = entityDao.search(query)
    if (resources.nonEmpty) {
      val roleQuery = OqlBuilder.from[Integer](classOf[FuncPermission].getName, "fp")
        .where("fp.resource.app.name = :appName", app).where("fp.resource.name =:resourceName", name)
        .select("fp.role.id")
        .cacheable()
      val p = new Properties(resources.head, "id", "name", "title", "scope")
      p.put("roles", entityDao.search(roleQuery).toArray)
      p
    } else {
      new Properties
    }
  }

  @response
  @mapping("public")
  def pub(@param("app") app: String): Seq[Any] = {
    val query = OqlBuilder.from(classOf[FuncResource], "fr").where("fr.app.name=:name", app)
      .where("fr.scope=:scope", Scopes.Public)
      .cacheable()
    entityDao.search(query)
  }

}
