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
package org.openurp.platform.admin.action.bulletin

import java.time.LocalDate

import org.beangle.security.Securities
import org.beangle.webmvc.api.annotation.ignore
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.bulletin.model.Notice
import org.openurp.platform.config.model.App
import org.openurp.platform.user.model.{ User, UserCategory }

class NoticeAction extends RestfulAction[Notice] {

  override protected def indexSetting(): Unit = {
    put("userCategories", entityDao.getAll(classOf[UserCategory]))
  }

  override protected def editSetting(entity: Notice): Unit = {
    put("userCategories", entityDao.getAll(classOf[UserCategory]))
    put("apps", entityDao.getAll(classOf[App]))
  }

  @ignore
  override protected def saveAndRedirect(notice: Notice): View = {
    notice.publishedOn = LocalDate.now
    notice.operator = entityDao.findBy(classOf[User], "code", List(Securities.user)).head
    super.saveAndRedirect(notice)
  }
}
