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

  @mapping("{name}")
  @response
  def index(@param("name") username: String): Properties = {
    userService.get(username) match {
      case Some(user) =>
        val properties = new Properties()
        properties += ("id" -> user.id)
        properties += ("principal" -> user.code)
        properties += ("description" -> user.name)
        properties += ("accountExpired" -> user.accountExpired)
        properties += ("accountLocked" -> user.locked)
        properties += ("credentialExpired" -> user.credentialExpired)
        properties += ("enabled" -> user.enabled)

        val query = OqlBuilder.from(classOf[RoleMember], "m").where("m.member=true and m.user=:user", user).select("m.role.id")
        properties += ("authorities" -> entityDao.search(query).toSet)
        val details = new Properties()
        details += ("category" -> user.category.id)
        properties += ("details" -> details)
        properties
      case None => new Properties()
    }
  }
}
