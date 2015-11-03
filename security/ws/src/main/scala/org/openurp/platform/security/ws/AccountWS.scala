package org.openurp.platform.security.ws

import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.security.authc.{ Account, DefaultAccount }
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.{ mapping, param }
import org.openurp.platform.api.app.UrpApp
import org.openurp.platform.security.model.FuncPermission
import org.openurp.platform.security.service.UserService
import org.beangle.webmvc.api.annotation.response
import org.beangle.commons.collection.Properties
import org.beangle.webmvc.api.action.EntitySupport
import org.openurp.platform.security.model.User

/**
 * @author chaostone
 */
class AccountWS(userService: UserService, entityDao: EntityDao) extends ActionSupport with EntitySupport[User] {

  @mapping("{name}")
  @response
  def index(@param("name") username: String): Properties = {
    userService.get(username) match {
      case Some(user) =>
        val properties = new Properties()
        properties += ("principal" -> user.code)
        properties += ("description" -> user.name)
        properties += ("accountExpired" -> user.accountExpired)
        properties += ("accountLocked" -> user.locked)
        properties += ("credentialExpired" -> user.credentialExpired)
        properties += ("disabled" -> !user.enabled)

        val query = OqlBuilder.from(classOf[FuncPermission], "fp").join("fp.role.members", "m").where("m.member=true and m.user=:user", user)
          .where("fp.resource.app.name=:appName", UrpApp.name).where("fp.endAt is null or fp.endAt < :now)", new java.util.Date).select("fp.id")
        properties += ("authorities" -> entityDao.search(query).toSet)

        val details = new Properties()
        details += ("isRoot" -> userService.isRoot(user, UrpApp.name))
        properties += ("details" -> details)
        properties
      case None => null
    }
  }
}