package org.openurp.platform.kernel.service

import org.openurp.platform.kernel.model.AccessToken
import org.openurp.platform.kernel.model.App

trait TokenRepository {

  def getApp(token: String): Option[String]

  def getToken(app: App): Option[AccessToken]

  def put(app: App, token: String): AccessToken
}