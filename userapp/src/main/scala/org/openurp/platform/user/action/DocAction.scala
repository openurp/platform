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

import java.io.ByteArrayInputStream

import org.beangle.commons.activation.MediaTypes
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.security.Securities
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.{mapping, param}
import org.beangle.webmvc.api.view.{Stream, View}
import org.openurp.platform.bulletin.model.Doc
import org.openurp.platform.user.model.User

class DocAction extends ActionSupport {

  var entityDao: EntityDao = _

  def index(): View = {
    val me: User = entityDao.findBy(classOf[User], "code", List(Securities.user)).head
    val docQuery = OqlBuilder.from(classOf[Doc], "doc")
    docQuery.where("doc.userCategory=:category", me.category)
    docQuery.limit(1, 20)
    docQuery.orderBy("doc.updatedAt desc")
    val docs = entityDao.search(docQuery)
    put("docs", docs)
    forward()
  }

  private def decideContentType(fileName: String): String = {
    MediaTypes.get(Strings.substringAfterLast(fileName, "."), MediaTypes.ApplicationOctetStream).toString
  }

  @mapping("{id}")
  def info(@param("id") id: String): View = {
    val doc = entityDao.get(classOf[Doc], id.toLong)
    Stream(new ByteArrayInputStream(doc.file.content), this.decideContentType(doc.file.fileName),
      doc.file.fileName)
  }

}
