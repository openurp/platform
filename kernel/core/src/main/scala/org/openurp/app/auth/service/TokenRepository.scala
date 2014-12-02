package org.openurp.app.auth.service

import org.openurp.app.App
import org.openurp.app.model.AccessTokenBean

trait TokenRepository {

  def getApp(token: String): Option[String]

  def getToken(app: App): Option[AccessTokenBean]

  def put(app: App, token: String): AccessTokenBean
}