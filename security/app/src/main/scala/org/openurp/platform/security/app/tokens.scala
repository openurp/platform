package org.openurp.platform.security.app

import org.beangle.security.web.PreauthToken

object Token {
  val AppTokenName = "app_token"
  val UserTokenName = "access_token"
}

class UserToken(t: String) extends PreauthToken(t) {

  def ticket: String = principal.toString

  details += Token.UserTokenName -> principal.toString
}

class AppToken(t: String) extends PreauthToken(t) {

  def ticket: String = principal.toString

  details += Token.AppTokenName -> principal.toString
}