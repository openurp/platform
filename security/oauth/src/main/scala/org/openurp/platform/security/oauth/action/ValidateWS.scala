package org.openurp.platform.security.oauth.action

import org.beangle.commons.collection.Properties
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.{ param, response }
import org.openurp.platform.security.oauth.TokenRepository

class ValidateWS(val tokenRepository: TokenRepository) extends ActionSupport {

  @response
  def index(@param("token") token: String): Any = {
    val properties = new Properties
    if (null == token) {
      properties.put("error", "token needed")
    } else {
      tokenRepository.get(token) match {
        case Some(app) =>
          token
        case None =>
          properties.put("error", "cannot find app")
      }
    }
    properties
  }
}