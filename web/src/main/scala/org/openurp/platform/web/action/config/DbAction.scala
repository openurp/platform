/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2005, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.platform.web.action.config

import java.sql.DriverManager

import collection.mutable.ListBuffer

import org.beangle.webmvc.api.annotation.ignore
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.config.model.Db
import org.beangle.webmvc.api.view.View

class DbAction extends RestfulAction[Db] {

  override def simpleEntityName = "db"

  def test(): View = {
    val username = get("user", "")
    val password = get("password", "")
    val entities = getModels[Db](entityName, ids(simpleEntityName, entityDao.domain.getEntity(entityName).get.id.clazz))
    val result = new ListBuffer[Tuple2[Db, Boolean]]
    for (cfg <- entities) {
      try {
        val conn = DriverManager.getConnection(cfg.url.orNull, username, password)
        conn.close()
        result += cfg -> true
      } catch {
        case t: Throwable =>
          t.printStackTrace()
          result += cfg -> false
      }
    }
    put("result", result)
    forward()
  }

  protected override def editSetting(entity: Db): Unit = {
    val drivers = Map("postgresql" -> "PostgreSQL", "oracle" -> "Oracle", "mysql" -> "MySQL", "db2" -> "DB2", "sqlserver" -> "Microsoft SQL Server")
    put("drivers", drivers)
  }

}
