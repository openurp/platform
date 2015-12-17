package org.openurp.platform.security.app

import java.net.URL
import org.beangle.commons.io.IOs
import org.beangle.security.authc.{ AbstractAccountRealm, Account, AuthenticationToken, BadCredentialsException, DefaultAccount }
import org.openurp.platform.api.app.UrpApp
import org.openurp.platform.api.util.JSON
import org.openurp.platform.security.app.Token.UserTokenName

/**
 * 远程验证用户的access_token,加载用户权限
 */
class RemoteUserRealm extends AbstractAccountRealm {

  protected override def determinePrincipal(token: AuthenticationToken): Any = {
    RemoteAppService.validate(token.details(UserTokenName).toString, token)
  }

  protected override def credentialsCheck(token: AuthenticationToken, account: Account): Unit = {}

  protected override def loadAccount(principal: Any): Option[Account] = {
    val user = principal.asInstanceOf[SimpleUser]
    val account = new DefaultAccount(user.code, user.name)
    account.authorities = RemoteAppService.getRoles(principal.toString())
    Some(account)
  }

  override def supports(token: AuthenticationToken): Boolean = token.isInstanceOf[UserToken]
}
