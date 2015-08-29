
package org.openurp.platform.security.service.impl

import org.beangle.security.authc.{ Account, AccountStore, DefaultAccount }
import org.openurp.platform.security.service.UserService

class DaoUserStore(userService: UserService) extends AccountStore {
  def load(principal: Any): Option[Account] = {
    userService.get(principal.toString) match {
      case Some(user) =>
        val account = new DefaultAccount(user.name, user.id)
        account.accountExpired = user.accountExpired
        account.accountLocked = user.locked
        account.credentialExpired = user.credentialExpired
        account.category = user.category
        account.disabled = !user.enabled
        account.authorities = user.roles.map(role => new org.beangle.security.authz.Role(role.id))
        account.details += "credential" -> user.credential
        Some(account)
      case None => None
    }
  }
}


