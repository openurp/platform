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

import java.io.{ ByteArrayInputStream, File, FileInputStream }

import org.beangle.commons.lang.Strings.substringAfterLast
import org.beangle.data.dao.EntityDao
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.{ mapping, param }
import org.beangle.webmvc.api.view.{ Stream, View }
import org.openurp.platform.user.model.Avatar
import org.beangle.webmvc.api.view.Status
import org.beangle.commons.activation.MimeTypes

class AvatarWS(entityDao: EntityDao) extends ActionSupport {

  @mapping("{avatarId}")
  def info(@param("avatarId") avatarId: String): View = {
    loadAvatar(avatarId) match {
      case Some(avatar) =>
        Stream(new ByteArrayInputStream(avatar.image), decideContentType(avatar.fileName), avatar.fileName)
      case None => Status.NotFound
    }
  }

  private def decideContentType(fileName: String): String = {
    MimeTypes.getMimeType(substringAfterLast(fileName, "."), MimeTypes.ApplicationOctetStream).toString
  }

  private def loadAvatar(avatarId: String): Option[Avatar] = {
    val avatar = entityDao.get(classOf[Avatar], avatarId)
    if (null == avatar) {
      None
    } else {
      Some(avatar)
    }
  }

}
