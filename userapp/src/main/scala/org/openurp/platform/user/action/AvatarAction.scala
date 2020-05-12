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
package org.openurp.platform.user.action

import javax.servlet.http.Part
import org.beangle.data.dao.EntityDao
import org.beangle.security.Securities
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.view.{Status, View}
import org.openurp.platform.user.model.User
import org.openurp.platform.user.service.AvatarService

class AvatarAction extends ActionSupport {

  var entityDao: EntityDao = _

  var avatarService: AvatarService = _

  def index(): View = {
    forward()
  }

  def upload(): View = {
    val users = entityDao.findBy(classOf[User], "code", List(Securities.user))
    if (users.isEmpty) {
      logger.warn("Cannot find user info of " + Securities.user)
      Status.NotFound
    } else {
      val user = users.head
      getAll("photo", classOf[Part]) foreach { p =>
        avatarService.save(user, p.getSubmittedFileName, p.getInputStream)
      }
    }
    forward()
  }
}
