package org.openurp.platform.oauth.service

import org.openurp.platform.config.model.AccessToken

trait TokenRepository {

  def get(token: String): Option[AccessToken]

  def put(token: AccessToken): Unit

  def remove(token: String): Unit
}