package org.openurp.platform.security.service.impl

import java.{ util => ju }

import org.beangle.data.model.dao.EntityDao
import org.openurp.platform.security.model.{ User, UserProfile }
import org.openurp.platform.security.model.Member
import org.openurp.platform.security.model.MemberShip
import org.openurp.platform.security.model.MemberShip.{ Granter, Manager, Member, Ship }
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

  import MemberShip._
  def getMembers(user: User, ship: Ship): Seq[Member] = {
    ship match {
      case Manager => user.members.filter(m => m.manager)
      case Granter => user.members.filter(m => m.granter)
      case Member => user.members.filter(m => m.member)
    }
  }

  //FIXME
  def isManagedBy(manager: User, user: User): Boolean = {
    true
  }

  //FIXME
  def isRoot(user: User): Boolean = {
    false
  }

  def create(creator: User, user: User): Unit = {
    //    user.creator = creator
    user.updatedAt = new ju.Date
    entityDao.saveOrUpdate(user)
    //    publish(new UserCreationEvent(Collections.singletonList(newUser)));
  }

  def updateState(manager: User, userIds: Array[java.lang.Long], active: Boolean): Int = {
    0
  }
  def remove(manager: User, user: User): Unit = {
    val removed = new collection.mutable.ListBuffer[User]
    if (isManagedBy(manager, user)) {
      entityDao.remove(entityDao.findBy(classOf[UserProfile], "user", List(user)), user);
      removed += user
    }
    //    entityDao.remove(removed)
    //    publish(new UserRemoveEvent(removed));
  }
}