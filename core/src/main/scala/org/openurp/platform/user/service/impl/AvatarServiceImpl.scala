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
package org.openurp.platform.user.service.impl

import java.io.InputStream

import org.beangle.commons.codec.digest.Digests
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.openurp.app.UrpApp
import org.openurp.platform.user.model.{Avatar, User}
import org.openurp.platform.user.service.AvatarService

class AvatarServiceImpl extends AvatarService {

  var entityDao: EntityDao = _

  def save(user: User, filename: String, is: InputStream): Unit = {
    val repo = UrpApp.getBlobRepository(true)
    val query = OqlBuilder.from(classOf[Avatar], "avatar")
    query.where("avatar.user=:user", user)
    val avatars = entityDao.search(query)
    var avatar: Avatar = null
    if (avatars.isEmpty) {
      avatar = new Avatar(user)
      avatar.id = Digests.md5Hex(user.code)
    } else {
      avatar = avatars.head
      if(null!=avatar.path) {
        repo.remove(avatar.path)
      }
    }
    val meta = repo.upload(s"/avatar/${user.beginOn.getYear}", is, filename, user.code + " " + user.name)
    user.avatarId = Some(avatar.id)
    avatar.fileName = meta.name
    avatar.updatedAt = meta.updatedAt
    avatar.path = meta.path
    entityDao.saveOrUpdate(avatar, user)
  }
}