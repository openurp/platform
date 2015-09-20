package org.openurp.platform.security.oauth.action

import java.util.UUID
import org.beangle.commons.collection.Properties
import org.beangle.data.jpa.dao.OqlBuilder
import org.beangle.data.model.dao.EntityDao
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.{ param, response }
import org.openurp.platform.kernel.model.App
import org.openurp.platform.security.oauth.TokenRepository
import org.openurp.platform.security.oauth.AccessToken
import java.{ util => ju }

class UserTokenWS(val tokenRepository: TokenRepository) extends ActionSupport {

  var entityDao: EntityDao = _

  @response
  def index(@param("app") app: String, @param("secret") secret: String, responseCode: String): Properties = {
    val query = OqlBuilder.from(classOf[App], "app").where("app.name=:name and app.secret=:secret", app, secret)
    val properties = new Properties
    val rs = entityDao.search(query)
    if (rs.isEmpty) {
      properties.put("error", "Incorrect app name or secret!")
    } else {
      val app = rs.head
      val token = new AccessToken
      token.id = generateAccessTokenId()
      token.appId = app.id
      token.expiredAt = this.generateExpiredAt()
      token.principal = getUser(app, responseCode)
      properties.put("token", token.id)
      properties.put("expiredAt", token.expiredAt)
    }
    properties
  }

  private def generateAccessTokenId(): String = {
    UUID.randomUUID().toString()
  }

  private def getUser(app: App, responseCode: String): String = {
    //if (app.trusted) responseCode.toLong
    //else {
    // throw new RuntimeException("Not supported.")
    // }
    "anonymous"
  }

  private def generateExpiredAt(): ju.Date = {
    val expiredAt = ju.Calendar.getInstance
    expiredAt.add(ju.Calendar.MONTH, 12)
    expiredAt.getTime
  }
}