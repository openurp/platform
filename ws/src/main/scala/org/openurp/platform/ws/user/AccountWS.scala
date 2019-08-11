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

import org.beangle.commons.collection.Properties
import org.beangle.data.dao.EntityDao
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.{mapping, param, response}
import org.openurp.platform.user.service.UserService

/**
 * @author chaostone
 */
class AccountWS(userService: UserService, entityDao: EntityDao) extends ActionSupport {

  @response
  @mapping("{userCode}")
  def index(@param("userCode") userCode: String): Properties = {
    userService.get(userCode) match {
      case Some(user) =>
        val properties = new Properties()
        properties += ("id" -> user.id)
        properties += ("principal" -> user.code)
        properties += ("description" -> user.name)
        properties += ("accountExpired" -> user.accountExpired)
        properties += ("accountLocked" -> user.locked)
        properties += ("credentialExpired" -> user.credentialExpired)
        properties += ("enabled" -> user.enabled)
        properties += ("authorities" -> user.roles.filter(_.member==true).map(_.role.id).mkString(","))
        val details = new Properties()
        details += ("category" -> user.category.id)
        properties += ("details" -> details)
        properties
      case None => new Properties()
    }
  }
}
