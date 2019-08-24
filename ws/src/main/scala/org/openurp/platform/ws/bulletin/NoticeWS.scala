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
package org.openurp.platform.ws.bulletin

import java.time.LocalDate

import org.beangle.commons.collection.Properties
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.webmvc.api.action.{ActionSupport, EntitySupport}
import org.beangle.webmvc.api.annotation.{mapping, param, response}
import org.openurp.platform.bulletin.model.{Notice, NoticeStatus}

class NoticeWS(entityDao: EntityDao) extends ActionSupport with EntitySupport[Notice] {

  @mapping(value = "{app}/{category}")
  @response
  def index(@param("app") app: String, @param("category") category: String): AnyRef = {
    val query = OqlBuilder.from(classOf[Notice], "notice")
    query.where("notice.userCategory.id=:category", category.toInt)
    query.where(":now between notice.beginOn and notice.endOn", LocalDate.now)
    query.where("notice.status=:status", NoticeStatus.Passed)
    query.orderBy("notice.sticky desc,notice.publishedAt desc")
    val notices = entityDao.search(query)
    notices.map(convertTitle)
  }

  @mapping(value = "{id}")
  def info(@param("id") id: String): AnyRef = {
    val query = OqlBuilder.from(classOf[Notice], "notice")
    query.where("notice.id=:id", id.toLong)
    query.where("notice.status=:status", NoticeStatus.Passed)
    val notices = entityDao.search(query)
    if (notices.nonEmpty) convert(notices.head) else null
  }

  private def convertTitle(notice: Notice): Properties = {
    new Properties(notice, "id", "title", "title", "createdAt", "popup", "sticky")
  }

  private def convert(notice: Notice): Properties = {
    new Properties(notice, "id", "title", "title", "createdAt", "popup", "sticky", "content")
  }
}
