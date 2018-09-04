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
package org.openurp.platform.admin.action.bulletin

import java.time.Instant

import org.beangle.security.Securities
import org.beangle.webmvc.api.annotation.ignore
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.bulletin.model.{ Attachment, Doc }
import org.openurp.platform.config.model.Domain
import org.openurp.platform.user.model.{ User, UserCategory }

import javax.servlet.http.Part

class DocAction extends RestfulAction[Doc] {

  override protected def indexSetting(): Unit = {
    put("userCategories", entityDao.getAll(classOf[UserCategory]))
  }

  override protected def editSetting(entity: Doc): Unit = {
    put("userCategories", entityDao.getAll(classOf[UserCategory]))
    put("domains", entityDao.getAll(classOf[Domain]))
  }

  @ignore
  override protected def removeAndRedirect(entities: Seq[Doc]): View = {
    try {
      val files = entities.map(d => d.file)
      entityDao.remove(entities, files)
      redirect("search", "info.remove.success")
    } catch {
      case e: Exception => {
        logger.info("removeAndForwad failure", e)
        redirect("search", "info.delete.failure")
      }
    }
  }

  @ignore
  override protected def saveAndRedirect(doc: Doc): View = {
    doc.updatedAt = Instant.now
    doc.uploadBy = entityDao.findBy(classOf[User], "code", List(Securities.user)).head
    getAll("docfile", classOf[Part]) foreach { docFile =>
      val attachment = Attachment(docFile.getSubmittedFileName, docFile.getInputStream)
      if (doc.file != null) {
        doc.file.merge(attachment)
      } else {
        doc.file = attachment
      }
      entityDao.saveOrUpdate(doc.file, doc)
    }
    super.saveAndRedirect(doc)
  }
}