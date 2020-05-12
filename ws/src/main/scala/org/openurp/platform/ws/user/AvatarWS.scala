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
package org.openurp.platform.ws.user

import org.beangle.cache.CacheManager
import org.beangle.commons.activation.MediaTypes
import org.beangle.commons.lang.ClassLoaders
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.webmvc.api.action.{ActionSupport, ServletSupport}
import org.beangle.webmvc.api.annotation.{mapping, param}
import org.beangle.webmvc.api.view.{Stream, View}
import org.openurp.app.UrpApp
import org.openurp.platform.user.model.Avatar

class AvatarWS(entityDao: EntityDao, cacheManager: CacheManager)
  extends ActionSupport with ServletSupport {

  var expireMinutes: Int = 60 * 24 * 7

  @mapping("default")
  def defaultAvatar(): View = {
    Stream(ClassLoaders.getResourceAsStream("org/openurp/platform/ws/default_avatar.jpg").get,
      MediaTypes.ImageJpeg.toString(), "default_avatar.jpg")
  }

  @mapping("{avatarId}")
  def info(@param("avatarId") avatarId: String): View = {
    loadAvatarPath(avatarId) match {
      case Some(a) => deliver(a); null
      case None => this.redirect("defaultAvatar")
    }
  }

  private def deliver(path: String): Unit = {
    UrpApp.getBlobRepository(true).path(path) match {
      case Some(p) => response.sendRedirect(p)
      case None => response.setStatus(404)
    }
  }

  private def loadAvatarPath(avatarId: String): Option[String] = {
    val query = OqlBuilder.from[String](classOf[Avatar].getName, "a")
    query.where("a.id = :id", avatarId)
    query.select("a.path")
    entityDao.search(query).headOption
  }

}
