package org.openurp.security.service

import org.openurp.security.model.UrpUserBean
import org.beangle.security.blueprint.service.UserService
import org.openurp.security.model.Member

trait UserManager extends UserService {

  def getMembers(user: UrpUserBean, ship: Member.Ship.Ship): Seq[Member]

  def isManagedBy(manager: UrpUserBean, user: UrpUserBean): Boolean

  def create(creator: UrpUserBean, user: UrpUserBean)

  def updateState(manager: UrpUserBean, userIds: Array[java.lang.Long], active: Boolean): Int

  def remove(creator: UrpUserBean, user: UrpUserBean)
}