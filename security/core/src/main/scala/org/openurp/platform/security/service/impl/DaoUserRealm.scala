package org.openurp.platform.security.service.impl

import org.beangle.security.authc.AuthenticationToken
import org.beangle.security.authc.BadCredentialsException
import org.beangle.security.authc.AbstractAccountRealm
import org.beangle.security.authc.Account
import org.beangle.commons.codec.digest.Digests

class DaoUserRealm(daoUserStore: DaoUserStore) extends AbstractAccountRealm {

  protected override def credentialsCheck(token: AuthenticationToken, account: Account): Unit = {
    val credential = account.details("credential")
    account.details = account.details - "credential"
    //Digests.md5Hex(token.credentials.toString)
    if (credential != token.credentials.toString) throw new BadCredentialsException("Incorrect password", token, null)
  }

  protected override def loadAccount(principal: Any): Option[Account] = {
    daoUserStore.load(principal)
  }
}