package org.openurp.platform.kernel.ws

import org.beangle.commons.collection.Properties
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.{ param, response }
import org.openurp.platform.kernel.service.TokenRepository

class ValidateWS(val tokenRepository: TokenRepository) extends ActionSupport {

  @response
  def index(@param("token") token: String): Any = {
    val properties = new Properties
    if (null == token) {
      properties.put("error", "token needed")
    } else {
      tokenRepository.getApp(token) match {
        case Some(app) =>
          properties.put("app_id", app)
        case None =>
          properties.put("error", "cannot find app")
      }
    }
    properties
  }
}