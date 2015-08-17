package org.openurp.platform.security.service

import org.openurp.platform.security.model.UrpUser
import org.beangle.security.blueprint.service.UserService
import org.openurp.platform.security.model.UrpMember

trait UserManager extends UserService {

  def getMembers(user: UrpUser, ship: UrpMember.Ship.Ship): Seq[UrpMember]

  def isManagedBy(manager: UrpUser, user: UrpUser): Boolean

  def create(creator: UrpUser, user: UrpUser)

  def updateState(manager: UrpUser, userIds: Array[java.lang.Long], active: Boolean): Int

  def remove(creator: UrpUser, user: UrpUser)
}