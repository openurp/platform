package org.openurp.platform.user.service

import org.openurp.platform.user.model.MemberShip
import org.openurp.platform.user.model.RoleMember
import org.openurp.platform.user.model.User

trait UserService {

  def get(code: String): Option[User]

  def get(id: Long): User

  def getUsers(id: Long*): Seq[User]

  def getRoles(user: User, ship: MemberShip.Ship): Seq[RoleMember]

  def isManagedBy(manager: User, user: User): Boolean

  def create(creator: User, user: User)

  def updateState(manager: User, userIds: Iterable[Long], active: Boolean): Int

  def remove(creator: User, user: User)

  def isRoot(user: User, appName: String): Boolean
}