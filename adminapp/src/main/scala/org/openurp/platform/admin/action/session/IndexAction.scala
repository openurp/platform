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
package org.openurp.platform.admin.action.session

import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.data.jdbc.query.JdbcExecutor
import org.beangle.webmvc.api.view.View
import org.beangle.commons.collection.Order
import javax.sql.DataSource
import java.sql.Timestamp

class IndexAction(ds: DataSource) extends ActionSupport {
  val jdbcExecutor = new JdbcExecutor(ds)

  def index(): View = {
    var sql = "select id,principal,description,ip,agent,os,login_at,last_access_at from session.session_infoes s"
    get(Order.OrderStr).foreach { o =>
      sql += (" order by " + o)
    }
    val list = jdbcExecutor.query(sql)
    val datas = list.map { d =>
      val info = new SessionInfo()
      info.id = d(0).asInstanceOf[String]
      info.principal = d(1).asInstanceOf[String]
      info.description = Option(d(2).asInstanceOf[String])
      info.ip = Option(d(3).asInstanceOf[String])
      info.agent = Option(d(4).asInstanceOf[String])
      info.os = Option(d(5).asInstanceOf[String])
      info.loginAt = d(6).asInstanceOf[Timestamp].toInstant
      info.lastAccessAt = d(7).asInstanceOf[Timestamp].toInstant
      info
    }
    put("sessionInfoes", datas)
    forward()
  }

}
