
package org.openurp.platform.security.action

import org.beangle.security.authc.{ Authenticator, UsernamePasswordAuthenticationToken }
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.param
import org.beangle.webmvc.api.view.View
import org.beangle.security.mgt.SecurityManager
import org.beangle.security.session.SessionId
import org.beangle.webmvc.api.context.ContextHolder
import org.beangle.security.web.authc.WebDetails

class LoginAction(val securityManager: SecurityManager) extends ActionSupport {

  def index(): String = {
    forward()
  }

  def login(@param("username") username: String, @param("password") password: String): View = {
    val request = ContextHolder.context.request
    val token = new UsernamePasswordAuthenticationToken(username, password)
    token.details = WebDetails.get(request)
    securityManager.login(token, SessionId(request.getSession.getId))
    redirect(to(classOf[IndexAction], "index"), null);
  }
}