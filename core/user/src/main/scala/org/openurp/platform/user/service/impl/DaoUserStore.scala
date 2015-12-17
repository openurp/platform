
package org.openurp.platform.user.service.impl

import org.beangle.data.dao.EntityDao
import org.beangle.security.authc.{ Account, AccountStore, DefaultAccount }
import org.openurp.platform.user.service.UserService

class DaoUserStore(userService: UserService, entityDao: EntityDao) extends AccountStore {

  def load(principal: Any): Option[Account] = {

    userService.get(principal.toString) match {
      case Some(user) =>
        val account = new DefaultAccount(user.code, user.name)
        account.accountExpired = user.accountExpired
        account.accountLocked = user.locked
        account.credentialExpired = user.credentialExpired
        account.disabled = !user.enabled
        account.authorities = user.roles.map(r => r.id).toSet
        account.details += "category" -> user.category.id
        Some(account)
      case None => None
    }
  }
}
