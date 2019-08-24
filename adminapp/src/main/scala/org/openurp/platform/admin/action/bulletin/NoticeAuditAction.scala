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

import java.time.Instant

import org.beangle.security.Securities
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.{mapping, param}
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.EntityAction
import org.openurp.platform.bulletin.model.{Notice, NoticeStatus}
import org.openurp.platform.config.model.App
import org.openurp.platform.user.model.{User, UserCategory}

class NoticeAuditAction extends ActionSupport with EntityAction[Notice] {

  def index(): View = {
    put("userCategories", entityDao.getAll(classOf[UserCategory]))
    put("apps", entityDao.getAll(classOf[App]))
    forward()
  }

  def search(): View = {
    put("notices", entityDao.search(getQueryBuilder))
    forward()
  }

  def audit(): View = {
    val notices = entityDao.find(classOf[Notice], longIds("notice"))
    val passed = getBoolean("passed", defaultValue = false)
    val me = entityDao.findBy(classOf[User], "code", List(Securities.user)).head
    notices foreach { notice =>
      notice.auditor = Some(me)
      notice.updatedAt = Instant.now
      if (passed) {
        notice.publishedAt = Some(Instant.now)
        notice.status = NoticeStatus.Passed
      } else {
        notice.publishedAt = None
        notice.status = NoticeStatus.Unpassed
      }
    }
    entityDao.saveOrUpdate(notices)
    redirect("search", "info.save.success")
  }

  @mapping(value = "{id}")
  def info(@param("id") id: String): View = {
    put(simpleEntityName, getModel[Notice](entityName, convertId(id)))
    forward()
  }
}
