package org.openurp.platform.security.service

import org.openurp.platform.security.model.{ Member, MemberShip, User }
import org.openurp.platform.kernel.model.App

trait UserManager extends UserService {

  def getMembers(user: User, ship: MemberShip.Ship): Seq[Member]

  def isManagedBy(manager: User, user: User): Boolean

  def create(creator: User, user: User)

  def updateState(manager: User, userIds: Iterable[Long], active: Boolean): Int

  def remove(creator: User, user: User)
}