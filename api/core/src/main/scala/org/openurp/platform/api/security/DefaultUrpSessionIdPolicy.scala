package org.openurp.platform.api.security

import org.beangle.security.web.session.CookieSessionIdPolicy
import org.openurp.platform.api.security.id.DefaultIdGenerator

import javax.servlet.http.HttpServletRequest

/**
 * @author chaostone
 */
class DefaultUrpSessionIdPolicy extends CookieSessionIdPolicy("URP_SID") {
  private val sessionIdGenerator = new DefaultIdGenerator("URP-", 35)

  protected override def generateId(request: HttpServletRequest): String = {
    sessionIdGenerator.nextid()
  }

}