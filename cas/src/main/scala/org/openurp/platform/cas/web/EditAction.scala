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
package org.openurp.platform.cas.web

import org.beangle.data.dao.EntityDao
import org.beangle.webmvc.api.action.{ ActionSupport, ServletSupport }
import org.beangle.webmvc.api.annotation.mapping
import org.beangle.webmvc.api.view.View
import org.beangle.security.Securities
import org.openurp.platform.user.model.User
import org.beangle.security.codec.DefaultPasswordEncoder
import java.time.Instant

class EditAction extends ActionSupport with ServletSupport {

  var entityDao: EntityDao = _

  @mapping(value = "")
  def index(): View = {
    put("principal", Securities.session.get.principal)
    forward()
  }

  def save(): View = {
    get("password") foreach { p =>
      val users = entityDao.findBy(classOf[User], "code", List(Securities.user))
      if (users.size == 1) {
        val user = users.head
        user.password = DefaultPasswordEncoder.generate(p, null, "sha")
        user.updatedAt = Instant.now
      }
      entityDao.saveOrUpdate(users)
    }
    redirect("index", "&updated=1", "info.save.success")
  }
}
