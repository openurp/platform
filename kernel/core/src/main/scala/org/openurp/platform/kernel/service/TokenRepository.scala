package org.openurp.platform.kernel.service

import org.openurp.platform.kernel.model.AccessToken

trait TokenRepository {

  def get(token: String): Option[AccessToken]

  def put(token: AccessToken): Unit

  def remove(token: String): Unit
}