package org.openurp.kernel.app.ws.security

import java.util.UUID

import org.beangle.commons.collection.Properties
import org.beangle.data.jpa.dao.OqlBuilder
import org.beangle.data.model.dao.EntityDao
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.{ param, response }
import org.openurp.kernel.app.App
import org.openurp.kernel.app.auth.service.TokenRepository

class LoginWS(val tokenRepository: TokenRepository) extends ActionSupport {

  var entityDao: EntityDao = _

  @response
  def index(@param("app") app: String, @param("secret") secret: String): Properties = {
    val query = OqlBuilder.from(classOf[App], "app").where("app.name=:name and app.secret=:secret", app, secret)
    val properties = new Properties
    val rs = entityDao.search(query)
    if (rs.isEmpty) {
      properties.put("error", "Incorrect app name or secret!")
    } else {
      val app = rs.head
      val token = tokenRepository.getToken(app) match {
        case Some(t) => t
        case None =>
          val ts = UUID.randomUUID().toString()
          tokenRepository.put(app, ts)
      }
      properties.put("token", token.token)
      properties.put("expiredAt", token.expiredAt)
    }
    properties
  }
}