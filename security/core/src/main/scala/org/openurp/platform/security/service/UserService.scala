package org.openurp.platform.security.service

import org.openurp.platform.security.model.User

trait UserService {

  def get(code: String): Option[User]

  def get(id: Long): User

  def getUsers(id: Long*): Seq[User]

  def isRoot(user: User,appName:String): Boolean
}