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

import java.time.{Instant, LocalDate}

import javax.servlet.http.Part
import org.beangle.data.dao.OqlBuilder
import org.beangle.security.Securities
import org.beangle.webmvc.api.annotation.ignore
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.bulletin.model._
import org.openurp.platform.config.model.{App, AppType}
import org.openurp.platform.user.model.{User, UserCategory}

class NoticeAction extends RestfulAction[Notice] {

  override protected def indexSetting(): Unit = {
    put("userCategories", entityDao.getAll(classOf[UserCategory]))
    put("apps", getWebApps)
  }

  private def getWebApps: Iterable[App] = {
    val query = OqlBuilder.from(classOf[App], "app").where("app.enabled =true")
    query.where("app.appType.name=:appType", AppType.Webapp)
    query.orderBy("app.indexno")
    query.cacheable()
    entityDao.search(query)
  }

  override protected def editSetting(entity: Notice): Unit = {
    put("userCategories", entityDao.getAll(classOf[UserCategory]))
    put("apps", getWebApps)
    if (null == entity.status) {
      entity.status = NoticeStatus.Draft
    }
  }


  override protected def removeAndRedirect(notices: Seq[Notice]): View = {
    val docs = notices.map(_.docs).flatten
    entityDao.remove(notices, docs)
    redirect("search", "info.remove.success")
  }

  override protected def getQueryBuilder: OqlBuilder[Notice] = {
    val builder = super.getQueryBuilder
    getInt("userCategory.id") foreach { categoryId =>
      builder.join("notice.userCategories", "uc")
      builder.where("uc.id=:userCategoryId", categoryId)
    }
    getBoolean("active") foreach { active =>
      if (active) {
        builder.where(":now between notice.beginOn and notice.endOn", LocalDate.now)
      } else {
        builder.where(" not(:now between notice.beginOn and notice.endOn)", LocalDate.now)
      }
    }
    builder
  }

  @ignore
  override protected def saveAndRedirect(notice: Notice): View = {
    notice.updatedAt = Instant.now
    if (null == notice.createdAt) {
      notice.createdAt = notice.updatedAt
    }
    val words = entityDao.getAll(classOf[SensitiveWord]).map(_.content).toSet
    val results = SensitiveFilter(words).matchedWords(notice.content)
    if (results.nonEmpty) {
      addFlashMessage("找到敏感词汇:" + results.mkString(","))
      throw new RuntimeException("找到敏感词汇:" + results.mkString(","))
    }
    notice.operator = entityDao.findBy(classOf[User], "code", List(Securities.user)).head
    notice.userCategories.clear()
    notice.userCategories ++= entityDao.find(classOf[UserCategory], intIds("userCategory"))
    getAll("notice_doc", classOf[Part]) foreach { docFile =>
      val doc = new Doc
      doc.app = notice.app
      val attachment = Attachment(docFile.getSubmittedFileName, docFile.getInputStream)
      doc.file = attachment
      doc.name = attachment.fileName
      doc.uploadBy = notice.operator
      doc.userCategories ++= notice.userCategories
      doc.updatedAt = Instant.now
      entityDao.saveOrUpdate(doc.file, doc)
      notice.docs += doc
    }
    notice.status = NoticeStatus.Submited
    super.saveAndRedirect(notice)
  }
}
