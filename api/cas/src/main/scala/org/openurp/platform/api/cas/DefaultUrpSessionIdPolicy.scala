package org.openurp.platform.api.cas

import org.beangle.security.web.session.CookieSessionIdPolicy
import org.openurp.platform.api.cas.id.DefaultIdGenerator
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.Cookie

/**
 * @author chaostone
 */
class DefaultUrpSessionIdPolicy(cookieName: String = "URP_SID") extends CookieSessionIdPolicy(cookieName) {
  private val sessionIdGenerator = new DefaultIdGenerator("URP-", 35)

  protected def newId(request: HttpServletRequest): String = {
    sessionIdGenerator.nextid()
  }

  protected override def createCookie(value: String): Cookie = {
    val cookie = new Cookie(cookieName, value)
    if (domain != null) cookie.setDomain(domain)
    cookie.setPath(path)
    cookie
  }

}