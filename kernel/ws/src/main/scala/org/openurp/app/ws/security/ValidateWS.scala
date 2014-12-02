package org.openurp.app.ws.security

import org.beangle.webmvc.api.action.ActionSupport
import org.openurp.app.auth.service.TokenRepository
import org.beangle.webmvc.api.annotation.mapping
import org.beangle.webmvc.api.annotation.response
import org.beangle.webmvc.api.annotation.param
import org.beangle.commons.collection.Properties

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