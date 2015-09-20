package org.openurp.platform.security.app

import org.beangle.security.mgt.SecurityManager
import org.beangle.security.web.{ AbstractPreauthFilter, PreauthToken }

import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }
/**
 * Processes a App service ticket.
 */
class TokenPreauthFilter(securityManager: SecurityManager) extends AbstractPreauthFilter(securityManager) {

  override def getPreauthToken(request: HttpServletRequest, response: HttpServletResponse): PreauthToken = {
    var ticket = request.getParameter(Token.UserTokenName)
    if (ticket == null) {
      ticket = request.getParameter(Token.AppTokenName)
      if (null == ticket) null
      else new AppToken(ticket)
    } else {
      new UserToken(ticket)
    }
  }
}
