package org.openurp.platform.security.service

import org.openurp.platform.security.model.User

trait UserService {

  def get(code: String): Option[User]

  def get(id: java.lang.Long): User

  def getUsers(id: java.lang.Long*): Seq[User]

  def isRoot(user: User): Boolean
}