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
package org.openurp.platform.admin.action.config

import java.sql.DriverManager

import org.beangle.commons.lang.{Strings, Throwables}
import org.beangle.data.jdbc.vendor.Vendors
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.app.util.AesEncryptor
import org.openurp.platform.config.model.{Credential, Db}

class DbAction extends RestfulAction[Db] {

  override def simpleEntityName = "db"

  def testSetting(): View = {
    val entities = getModels[Db](entityName, ids(simpleEntityName, entityDao.domain.getEntity(entityName).get.id.clazz))
    put("credentials", entityDao.getAll(classOf[Credential]))
    put("datasource", entities.head)
    forward()
  }

  def test(): View = {
    var username = get("username", "")
    var password = get("password", "")
    put("credentials", entityDao.getAll(classOf[Credential]))
    val entities = getModels[Db](entityName, ids(simpleEntityName, entityDao.domain.getEntity(entityName).get.id.clazz))
    val cfg = entities.head

    val useCredential = getBoolean("use_credential", false)
    try {
      if (useCredential) {
        val credential = entityDao.get(classOf[Credential], intId("credential"))
        val key = get("key").orNull
        username = credential.username
        password = new AesEncryptor(key).decrypt(credential.password)
      }
      if (Strings.isNotBlank(username) && Strings.isNotBlank(password)) {

        val url =
          cfg.url match {
            case None =>
              Class.forName(Vendors.drivers(cfg.driver).className)
              var format = Vendors.drivers(cfg.driver).urlformats.head
              format = Strings.replace(format, "<host>", cfg.serverName)
              format = Strings.replace(format, "<port>", cfg.portNumber.toString)
              format = Strings.replace(format, "<database_name>", cfg.databaseName)
              "jdbc:" + cfg.driver + ":" + format
            case Some(u) => u
          }
        val conn = DriverManager.getConnection(url, username, password)
        val msg = new StringBuilder
        msg.append("DatabaseProductName:").append(conn.getMetaData.getDatabaseProductName)
        msg.append("<br>DatabaseProductVersion:").append(conn.getMetaData.getDatabaseProductVersion)
        put("msg", msg.toString)
        conn.close()
        put("passed", true)

      }
    } catch {
      case t: Throwable =>
        put("msg", Throwables.stackTrace(t))
        put("passed", false)
    }
    forward()
  }


  protected override def editSetting(entity: Db): Unit = {
    val drivers = Map("postgresql" -> "PostgreSQL", "oracle" -> "Oracle", "mysql" -> "MySQL", "db2" -> "DB2", "sqlserver" -> "Microsoft SQL Server")
    put("drivers", drivers)
  }

}
