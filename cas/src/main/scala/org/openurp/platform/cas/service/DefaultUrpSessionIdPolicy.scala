package org.openurp.platform.cas.service

import org.beangle.security.web.session.CookieSessionIdPolicy

import javax.servlet.http.HttpServletRequest
import org.beangle.ids.cas.id.impl.DefaultIdGenerator

/**
 * @author chaostone
 */
class DefaultUrpSessionIdPolicy extends CookieSessionIdPolicy("URP_SID") {
  private val sessionIdGenerator = new DefaultIdGenerator("URP-", 35)

  protected override def generateId(request: HttpServletRequest): String = {
    sessionIdGenerator.nextid()
  }

}