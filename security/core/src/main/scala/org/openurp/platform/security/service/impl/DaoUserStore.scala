
package org.openurp.platform.security.service.impl

import org.beangle.security.authc.{ Account, AccountStore, DefaultAccount }
import org.openurp.platform.security.service.UserService
import org.openurp.platform.api.app.AppConfig
import org.beangle.data.model.dao.EntityDao
import org.beangle.data.jpa.dao.OqlBuilder
import org.openurp.platform.security.model.SessionProfileBean

class DaoUserStore(userService: UserService, entityDao: EntityDao) extends AccountStore {

  def load(principal: Any): Option[Account] = {

    userService.get(principal.toString) match {
      case Some(user) =>
        val account = new DefaultAccount(user.code, user.name)
        account.accountExpired = user.accountExpired
        account.accountLocked = user.locked
        account.credentialExpired = user.credentialExpired
        account.disabled = !user.enabled
        account.authorities = user.roles.filter(role => role.app.name == AppConfig.appName).map(role => new org.beangle.security.authz.Role(role.id))
        account.details += "credential" -> user.credential
        Some(account)
      case None => None
    }
  }
}
