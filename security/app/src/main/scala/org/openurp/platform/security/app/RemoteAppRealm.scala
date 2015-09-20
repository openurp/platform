package org.openurp.platform.security.app

import java.net.URL
import org.beangle.commons.io.IOs
import org.beangle.security.authc.{ AbstractAccountRealm, Account, AccountStore, AuthenticationToken, BadCredentialsException }
import org.openurp.platform.api.app.AppConfig
import org.openurp.platform.security.app.Token.AppTokenName
import org.openurp.platform.api.ws.ServiceConfig
import org.beangle.security.authc.DefaultAccount
import org.openurp.platform.api.util.JSON

/**
 * 远程验证app的token，加载app权限
 */
class RemoteAppRealm extends AbstractAccountRealm {

  protected override def determinePrincipal(token: AuthenticationToken): Any = {
    RemoteAppService.validate(token.details(AppTokenName).toString, token)
  }

  protected override def credentialsCheck(token: AuthenticationToken, account: Account): Unit = {}

  protected override def loadAccount(principal: Any): Option[Account] = {
    val user = principal.asInstanceOf[SimpleUser]
    val account = new DefaultAccount(user.name, user.description)
    account.authorities = RemoteAppService.getAppPermissions(principal.toString)
    Some(account)
  }

  override def supports(token: AuthenticationToken): Boolean = token.isInstanceOf[AppToken]
}
