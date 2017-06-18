package org.openurp.platform.ws.user

import org.beangle.commons.collection.Properties
import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.webmvc.api.action.{ ActionSupport, EntitySupport }
import org.beangle.webmvc.api.annotation.{ mapping, param, response }
import org.openurp.platform.user.model.{ RoleMember, User }
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
        properties += ("authorities" -> user.roles.filter(_.member==true).map(_.id).toSet)
        val details = new Properties()
        details += ("category" -> user.category.id)
        properties += ("details" -> details)
        properties
      case None => new Properties()
    }
  }
}
