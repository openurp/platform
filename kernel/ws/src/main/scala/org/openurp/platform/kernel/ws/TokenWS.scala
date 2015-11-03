package org.openurp.platform.kernel.ws

import java.{ util => ju }
import java.util.UUID
import org.beangle.commons.collection.Properties
import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.{ param, response }
import org.openurp.platform.kernel.model.{ AccessToken, App }
import org.openurp.platform.kernel.service.TokenRepository
import org.openurp.platform.kernel.service.AppService

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