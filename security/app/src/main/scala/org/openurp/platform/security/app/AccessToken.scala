package org.openurp.platform.security.app

import org.beangle.security.web.PreauthToken

class AccessToken(t: String) extends PreauthToken(t) {

  def ticket: String = principal.toString

  details += AuthConfig.TokenName -> principal.toString
}