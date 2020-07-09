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
package org.openurp.platform.portal.action

import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.security.Securities
import org.beangle.webmvc.api.action.{ActionSupport, ServletSupport}
import org.beangle.webmvc.api.annotation.mapping
import org.beangle.webmvc.api.view.View
import org.openurp.app.Urp
import org.openurp.app.web.NavContext
import org.openurp.platform.bulletin.model.{Doc, Notice}
import org.openurp.platform.config.service.impl.DomainService
import org.openurp.platform.user.model.User

class IndexAction extends ActionSupport with ServletSupport {
  var entityDao: EntityDao = _

  var domainService: DomainService = _

  @mapping("")
  def index(): View = {
    val ctx = NavContext.get(request)
    put("nav", ctx)

    domainService.getDomain
    put("domain", domainService.getDomain)
    put("urp", Urp)
    forward()
  }

  def welcome(): View = {
    val me: User = entityDao.findBy(classOf[User], "code", List(Securities.user)).head
    val docQuery = OqlBuilder.from(classOf[Doc], "doc")
    docQuery.join("doc.userCategories", "uc")
    docQuery.where("uc.id=:category", me.category.id)
    docQuery.where("doc.archived=false")
    docQuery.limit(1, 10)
    docQuery.orderBy("doc.updatedAt desc")
    val docs = entityDao.search(docQuery)

    val noticeQuery = OqlBuilder.from(classOf[Notice], "notice")
    noticeQuery.join("notice.userCategories", "uc")
    noticeQuery.where("uc.id=:category", me.category.id)
    noticeQuery.where("notice.archived=false")
    noticeQuery.limit(1, 10)
    noticeQuery.orderBy("notice.publishedAt desc")
    val notices = entityDao.search(noticeQuery)

    put("docs", docs)
    put("notices", notices)
    put("user", me)
    put("webappBase", Urp.webapp)
    forward()
  }

  def logout(): View = {
    redirect(to(Urp.cas + "/logout"), null)
  }
}
