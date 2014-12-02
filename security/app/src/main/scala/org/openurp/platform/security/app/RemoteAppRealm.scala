package org.openurp.platform.security.app

import java.net.URL
import org.beangle.commons.io.IOs
import org.beangle.security.authc.{ AbstractAccountRealm, Account, AccountStore, AuthenticationToken, BadCredentialsException }
import org.openurp.platform.app.App
import org.openurp.platform.security.app.AuthConfig.TokenName
import org.openurp.platform.ws.ServiceConfig
import org.beangle.security.authc.DefaultAccount

class RemoteAppRealm extends AbstractAccountRealm {

  protected override def determinePrincipal(token: AuthenticationToken): String = {
    val ticket = token.details(TokenName).toString
    val url = ServiceConfig.wsBase + "/app/" + App.name + "/security/validate.json?token=" + ticket
    val result = IOs.readString(new URL(url).openStream())
    val prefix = "app_id\":\""
    val start = result.indexOf(prefix)
    if (-1 == start) {
      throw new BadCredentialsException("Bad credentials :" + token.details(TokenName), token, null)
    } else {
      result.substring(start + prefix.length, result.indexOf("\"", start + prefix.length))
    }
  }

  protected override def credentialsCheck(token: AuthenticationToken, account: Account): Unit = {}

  protected override def loadAccount(principal: Any): Option[Account] = {
    //FIXME
    //load app info using app/a
    Some(new DefaultAccount(principal, principal))
  }

  override def supports(token: AuthenticationToken): Boolean = token.isInstanceOf[AccessToken]
}
