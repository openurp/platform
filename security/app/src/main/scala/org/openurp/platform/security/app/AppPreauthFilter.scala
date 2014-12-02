package org.openurp.platform.security.app


import org.beangle.security.mgt.SecurityManager
import org.beangle.security.web.{AbstractPreauthFilter, PreauthToken}

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
/**
 * Processes a App service ticket.
 */
class AppPreauthFilter(securityManager: SecurityManager) extends AbstractPreauthFilter(securityManager) {

  override def getPreauthToken(request: HttpServletRequest, response: HttpServletResponse): PreauthToken = {
    val ticket = request.getParameter(AuthConfig.TokenName)
    if (ticket == null) {
      null
    } else {
      new AccessToken(ticket)
    }
  }
}
