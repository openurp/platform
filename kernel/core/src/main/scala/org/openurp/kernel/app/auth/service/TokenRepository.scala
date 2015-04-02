package org.openurp.kernel.app.auth.service

import org.openurp.kernel.app.App
import org.openurp.kernel.app.model.AccessTokenBean

trait TokenRepository {

  def getApp(token: String): Option[String]

  def getToken(app: App): Option[AccessTokenBean]

  def put(app: App, token: String): AccessTokenBean
}