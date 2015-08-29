
package org.openurp.platform.security.service.impl

import org.beangle.commons.codec.digest.Digests
import org.beangle.security.authc.{ AbstractAccountRealm, Account, AccountStore, AuthenticationToken, BadCredentialsException, DefaultAccount }
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
        account.authorities = user.roles.map(role => new org.beangle.security.authz.Role(role.name))
        account.details += "credential" -> user.credential
        Some(account)
      case None => None
    }
  }
}

class DaoUserRealm(daoUserStore: DaoUserStore) extends AbstractAccountRealm {

  protected override def credentialsCheck(token: AuthenticationToken, account: Account): Unit = {
    val credential = account.details("credential")
    account.details = account.details - "credential"
    if (credential != Digests.md5Hex(token.credentials.toString)) throw new BadCredentialsException("Incorrect password", token, null)
  }

  protected override def loadAccount(principal: Any): Option[Account] = {
    daoUserStore.load(principal)
  }
}
