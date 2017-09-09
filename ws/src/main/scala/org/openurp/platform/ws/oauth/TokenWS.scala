/*
 * Beangle, Agile Development Scaffold and Toolkit
 *
 * Copyright (c) 2005-2017, Beangle Software.
 *
 * Beangle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beangle is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Beangle.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.platform.ws.oauth

import java.{ util => ju }
import java.util.UUID
import org.beangle.commons.collection.Properties
import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.{ param, response }
import org.openurp.platform.config.model.{ AccessToken, App }
import org.openurp.platform.oauth.service.TokenRepository
import org.openurp.platform.config.service.AppService

class TokenWS(tokenRepository: TokenRepository, appService: AppService) extends ActionSupport {

  var entityDao: EntityDao = _

  @response
  def login(@param("app") name: String, @param("secret") secret: String): Properties = {
    val properties = new Properties
    appService.getApp(name, secret) match {
      case None => properties.put("error", "Incorrect app name or secret!")
      case Some(app) =>
        val token = new AccessToken
        token.id = generateAccessTokenId()
        token.appId = app.id
        token.principal = app.name
        token.expiredAt = this.generateExpiredAt()
        tokenRepository.put(token)
        properties.put("token", token.id)
        properties.put("appId", app.id)
        properties.put("expiredAt", token.expiredAt)
    }
    properties
  }

  @response
  def validate(@param("token") token: String): Any = {
    val properties = new Properties
    if (null == token) {
      properties.put("error", "token needed")
    } else {
      tokenRepository.get(token) match {
        case Some(app) => app
        case None => properties.put("error", "cannot find app"); properties
      }
    }
  }

  private def generateAccessTokenId(): String = {
    UUID.randomUUID().toString()
  }

  private def generateExpiredAt(): ju.Date = {
    val expiredAt = ju.Calendar.getInstance
    expiredAt.add(ju.Calendar.MONTH, 12)
    expiredAt.getTime
  }
}