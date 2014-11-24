package org.openurp.platform.app.service

trait AppService {

  def isPublic(resource: String): Boolean

  def isPermitted(appId: String, resource: String, method: String): Boolean
}