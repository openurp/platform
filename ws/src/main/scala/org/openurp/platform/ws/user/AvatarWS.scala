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
package org.openurp.platform.ws.user

import java.io.ByteArrayInputStream
import java.time.ZoneOffset

import org.beangle.cache.{ Cache, CacheManager }
import org.beangle.commons.activation.MimeTypes
import org.beangle.commons.io.IOs
import org.beangle.commons.lang.Strings.substringAfterLast
import org.beangle.commons.web.util.RequestUtils
import org.beangle.data.dao.EntityDao
import org.beangle.webmvc.api.action.{ ActionSupport, ServletSupport }
import org.beangle.webmvc.api.annotation.{ mapping, param }
import org.beangle.webmvc.api.util.CacheControl
import org.beangle.webmvc.api.view.View
import org.openurp.platform.user.model.Avatar

import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }
import org.beangle.commons.bean.Initializing
import java.time.LocalDateTime
import org.beangle.commons.lang.ClassLoaders
import java.io.ByteArrayOutputStream
import java.time.Instant

class AvatarWS(entityDao: EntityDao, cacheManager: CacheManager)
  extends ActionSupport with ServletSupport with Initializing {

  var expireMinutes = 60 * 24 * 7

  var cache: Cache[String, Avatar] = _

  var defaultOne: Avatar = _

  override def init() {
    cache = cacheManager.getCache("avatar", classOf[String], classOf[Avatar])
    defaultOne = new Avatar
    defaultOne.fileName = "default_avatar.jpg"
    defaultOne.updatedAt = Instant.now
    ClassLoaders.getResourceAsStream("org/openurp/platform/ws/default_avatar.jpg") foreach {
      is =>
        val out = new ByteArrayOutputStream()
        IOs.copy(is, out)
        defaultOne.image = out.toByteArray()
    }
  }

  @mapping("default")
  def defaultAvatar(): View = {
    deliver(defaultOne)
    null
  }

  @mapping("{avatarId}")
  def info(@param("avatarId") avatarId: String): View = {
    val avatar = loadAvatar(avatarId)
    if (null == avatar) return this.redirect("defaultAvatar")
    deliver(avatar)
    null
  }

  private def deliver(avatar: Avatar): Unit = {
    val lastModified = avatar.updatedAt.toEpochMilli()
    if (etagChanged(String.valueOf(lastModified), request, response)) {
      CacheControl.expiresAfter(expireMinutes, response)
      response.setDateHeader("Last-Modified", lastModified)
      val stream = new ByteArrayInputStream(avatar.image)
      response.setContentType(decideContentType(avatar.fileName))
      RequestUtils.setContentDisposition(response, avatar.fileName)
      IOs.copy(stream, response.getOutputStream)
    }
  }

  private def etagChanged(etag: String, request: HttpServletRequest, response: HttpServletResponse): Boolean = {
    val requestETag = request.getHeader("If-None-Match")
    // not modified, content is not sent - only basic headers and status SC_NOT_MODIFIED
    val changed = !etag.equals(requestETag)
    if (!changed) response.setStatus(HttpServletResponse.SC_NOT_MODIFIED)
    else response.setHeader("ETag", etag)
    changed
  }

  private def decideContentType(fileName: String): String = {
    MimeTypes.getMimeType(substringAfterLast(fileName, "."), MimeTypes.ApplicationOctetStream).toString
  }

  private def loadAvatar(avatarId: String): Avatar = {
    cache.get(avatarId) match {
      case None =>
        val avatar = entityDao.get(classOf[Avatar], avatarId)
        if (null == avatar) {
          null
        } else {
          cache.put(avatarId, avatar)
          avatar
        }
      case Some(a) => a
    }
  }
}
