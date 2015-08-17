package org.openurp.platform.security.service.internal

import org.openurp.platform.security.model.UrpMember
import org.beangle.security.blueprint.User
import org.openurp.platform.security.model.UrpUser
import org.beangle.data.model.dao.EntityDao
import java.{ util => ju }
import org.openurp.platform.security.model.UrpUserProfile
import org.openurp.platform.security.service.UserManager

class UserManagerImpl(val entityDao: EntityDao) extends UserManager {

  def get(code: String): Option[User] = {
    val rs = entityDao.findBy(classOf[User], "code", List(code))
    if (rs.isEmpty) None else Some(rs.head)
  }

  def get(id: java.lang.Long): User = {
    entityDao.get(classOf[User], id)
  }

  def getUsers(ids: java.lang.Long*): Seq[User] = {
    entityDao.find(classOf[User], ids.toList)
  }

  def getMembers(user: UrpUser, ship: UrpMember.Ship.Ship): Seq[UrpMember] = {
    ship match {
      case UrpMember.Ship.Manager => user.members.filter(m => m.manager)
      case UrpMember.Ship.Granter => user.members.filter(m => m.granter)
      case UrpMember.Ship.Member  => user.members.filter(m => m.member)
    }
  }

  //FIXME
  def isManagedBy(manager: UrpUser, user: UrpUser): Boolean = {
    true
  }

  //FIXME
  def isRoot(user: org.beangle.security.blueprint.User): Boolean = {
    false
  }

  def create(creator: UrpUser, user: UrpUser): Unit = {
//    user.creator = creator
    user.updatedAt = new ju.Date
    entityDao.saveOrUpdate(user)
    //    publish(new UserCreationEvent(Collections.singletonList(newUser)));
  }

  def updateState(manager: UrpUser, userIds: Array[java.lang.Long], active: Boolean): Int = {
    0
  }
  def remove(manager: UrpUser, user: UrpUser): Unit = {
    val removed = new collection.mutable.ListBuffer[User]
    if (isManagedBy(manager, user)) {
      entityDao.remove(entityDao.findBy(classOf[UrpUserProfile], "user", List(user)), user);
      removed += user
    }
    //    entityDao.remove(removed)
    //    publish(new UserRemoveEvent(removed));
  }
}