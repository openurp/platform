package org.openurp.platform.security.oauth

trait TokenRepository {

  def get(token: String): Option[AccessToken]

  def put(token: AccessToken): Unit

  def remove(token: String): Unit
}