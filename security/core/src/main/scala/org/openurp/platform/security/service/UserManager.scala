package org.openurp.platform.security.service

import org.openurp.platform.security.model.{ Member, MemberShip, User }

trait UserManager extends UserService {

  def getMembers(user: User, ship: MemberShip.Ship): Seq[Member]

  def isManagedBy(manager: User, user: User): Boolean

  def create(creator: User, user: User)

  def updateState(manager: User, userIds: Array[java.lang.Long], active: Boolean): Int

  def remove(creator: User, user: User)
}