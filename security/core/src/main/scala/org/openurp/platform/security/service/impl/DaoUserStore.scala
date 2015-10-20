
package org.openurp.platform.security.service.impl

import org.beangle.data.dao.EntityDao
import org.beangle.security.authc.{ Account, AccountStore, DefaultAccount }
import org.openurp.platform.api.app.AppConfig
import org.openurp.platform.security.service.UserService

class DaoUserStore(userService: UserService, entityDao: EntityDao) extends AccountStore {

  def load(principal: Any): Option[Account] = {

    userService.get(principal.toString) match {
      case Some(user) =>
        val account = new DefaultAccount(user.code, user.name)
        account.accountExpired = user.accountExpired
        account.accountLocked = user.locked
        account.credentialExpired = user.credentialExpired
        account.disabled = !user.enabled
        account.authorities = user.roles.filter(role => role.app.name == AppConfig.name).map(role => role.id)
        account.details += "credential" -> user.credential
        account.details += "isRoot" -> userService.isRoot(user, AppConfig.name)
        Some(account)
      case None => None
    }
  }
}
