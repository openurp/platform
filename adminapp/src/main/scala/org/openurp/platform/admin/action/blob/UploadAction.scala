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
package org.openurp.platform.admin.action.blob

import java.io.ByteArrayInputStream
import java.time.ZoneId

import org.beangle.data.dao.{EntityDao, LimitQuery, OqlBuilder, QueryPage}
import org.beangle.webmvc.api.action.{ActionSupport, ServletSupport}
import org.beangle.webmvc.api.view.View
import org.openurp.app.UrpApp
import org.openurp.platform.bulletin.model.Doc
import org.openurp.platform.user.model.Avatar

class UploadAction extends ActionSupport with ServletSupport{

  var entityDao: EntityDao = _

  def uploadAvatar(): View = {
    val query = OqlBuilder.from(classOf[Avatar], "a")
    query.orderBy("a.id").limit(1, 100)
    val page = new QueryPage(query.build().asInstanceOf[LimitQuery[Avatar]], entityDao)
    val iter = page.iterator
    val repo = UrpApp.getBlobRepository(true)
    var i=0
    while (iter.hasNext) {
      val avatar = iter.next()
      val user = avatar.user
      val is = new ByteArrayInputStream(avatar.image)
      val meta = repo.upload(s"/avatar/${user.beginOn.getYear}", is, avatar.fileName, user.code + " " + user.name)
      avatar.path = meta.path
      entityDao.saveOrUpdate(avatar)
      i+=1
    }
    response.getWriter.println(s"upload ${i} avatars")
    null
  }

  def uploadDoc(): View = {
    val query = OqlBuilder.from(classOf[Doc], "a")
    query.orderBy("a.id").limit(1, 100)
    val page = new QueryPage(query.build().asInstanceOf[LimitQuery[Doc]], entityDao)
    val iter = page.iterator
    val repo = UrpApp.getBlobRepository(true)
    var i=0
    while (iter.hasNext) {
      val doc = iter.next()
      val user = doc.uploadBy
      val is = new ByteArrayInputStream(doc.file.content)
      val meta = repo.upload(s"/doc/${doc.updatedAt.atZone(ZoneId.systemDefault()).getYear}", is, doc.name, user.code + " " + user.name)
      doc.size=meta.size
      doc.path = meta.path
      entityDao.saveOrUpdate(doc)
      i+=1
    }
    response.getWriter.println(s"upload ${i} docs")
    null
  }
}
