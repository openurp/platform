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

import javax.servlet.http.Part
import org.beangle.commons.activation.MediaTypes
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.OqlBuilder
import org.beangle.security.Securities
import org.beangle.webmvc.api.action.ServletSupport
import org.beangle.webmvc.api.annotation.{ignore, param}
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.app.UrpApp
import org.openurp.platform.bulletin.model.Doc
import org.openurp.platform.bulletin.service.DocService
import org.openurp.platform.config.model.{App, AppType}
import org.openurp.platform.user.model.{User, UserCategory}

class DocAction extends RestfulAction[Doc] with ServletSupport {

  var docService:DocService=_

  override protected def indexSetting(): Unit = {
    put("userCategories", entityDao.getAll(classOf[UserCategory]))
  }


  override protected def getQueryBuilder: OqlBuilder[Doc] = {
    val builder = super.getQueryBuilder
    getInt("userCategory.id") foreach { categoryId =>
      builder.join("doc.userCategories", "uc")
      builder.where("uc.id=:userCategoryId", categoryId)
    }
    builder
  }

  private def getWebApps: Iterable[App] = {
    val query = OqlBuilder.from(classOf[App], "app").where("app.enabled =true")
    query.where("app.appType.name=:appType", AppType.Webapp)
    query.orderBy("app.indexno")
    query.cacheable()
    entityDao.search(query)
  }

  override protected def editSetting(entity: Doc): Unit = {
    put("userCategories", entityDao.getAll(classOf[UserCategory]))
    put("apps", getWebApps)
  }

  def download(@param("id") id: String): View = {
    val doc = entityDao.get(classOf[Doc], id.toLong)
    UrpApp.getBlobRepository(true).path(doc.path) match {
      case Some(p) => response.sendRedirect(p)
      case None => response.setStatus(404)
    }
    null
  }

  @ignore
  override protected def removeAndRedirect(entities: Seq[Doc]): View = {
    try {
      val files = entities.map(d => d.file)
      entityDao.remove(entities, files)
      redirect("search", "info.remove.success")
    } catch {
      case e: Exception =>
        logger.info("removeAndForwad failure", e)
        redirect("search", "info.delete.failure")
    }
  }

  @ignore
  override protected def saveAndRedirect(doc: Doc): View = {
    doc.updatedAt = Instant.now
    doc.uploadBy = entityDao.findBy(classOf[User], "code", List(Securities.user)).head
    doc.userCategories.clear()
    doc.userCategories ++= entityDao.find(classOf[UserCategory], intIds("userCategory"))
    getAll("docfile", classOf[Part]) foreach { docFile =>
      docService.save(doc,docFile.getSubmittedFileName, docFile.getInputStream)
    }
    super.saveAndRedirect(doc)
  }
}
