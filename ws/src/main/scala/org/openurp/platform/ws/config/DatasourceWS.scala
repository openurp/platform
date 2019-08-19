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
package org.openurp.platform.ws.config

import org.beangle.commons.collection.Properties
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.webmvc.api.action.{ActionSupport, EntitySupport}
import org.beangle.webmvc.api.annotation.{mapping, param, response}
import org.openurp.platform.config.model.{App, DataSource}

class DatasourceWS(entityDao: EntityDao) extends ActionSupport with EntitySupport[DataSource] {

  @mapping(value = "{app}/{name}")
  @response
  def index(@param("app") app: String, @param("name") name: String): AnyRef = {
    val secret = get("secret", "")
    val apps = entityDao.findBy(classOf[App], "name", List(app))
    if (apps.isEmpty) return "error:error_app_name"
    val exist = apps.head
    if (exist.secret != secret) return "error:error_secret"

    val query = OqlBuilder.from(classOf[DataSource], "ds")
    query.where("ds.app=:app and ds.name=:key", exist, name)
    val set = entityDao.search(query)
    if (set != null && set.nonEmpty) {
      val rs = set.head
      val ds = new Properties
      ds.put("user", rs.credential.username)
      ds.put("password", "?" + rs.credential.password)
      ds.put("driver", rs.db.driver)
      if (rs.db.url.isDefined) {
        ds.put("url", rs.db.url.get)
      } else {
        ds.put("serverName", rs.db.serverName)
        ds.put("databaseName", rs.db.databaseName)
        ds.put("portNumber", rs.db.portNumber)
      }
      ds.put("maximumPoolSize", rs.maximumPoolSize)
      rs.db.properties foreach { case (k, v) =>
        ds.put(k, v)
      }
      ds
    } else "error:error_resource_key"
  }

}
