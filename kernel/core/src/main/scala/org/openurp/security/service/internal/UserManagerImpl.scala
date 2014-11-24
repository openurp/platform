package org.openurp.security.service.internal

import org.openurp.security.model.Member
import org.beangle.security.blueprint.User
import org.openurp.security.model.UrpUserBean
import org.beangle.data.model.dao.EntityDao
import java.{ util => ju }
import org.openurp.security.model.UserProfileBean
import org.openurp.security.service.UserManager

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

  def getMembers(user: UrpUserBean, ship: Member.Ship.Ship): Seq[Member] = {
    ship match {
      case Member.Ship.IsManager => user.members.filter(m => m.manager)
      case Member.Ship.IsGranter => user.members.filter(m => m.granter)
      case Member.Ship.IsMember  => user.members.filter(m => m.member)
    }
  }

  //FIXME
  def isManagedBy(manager: UrpUserBean, user: UrpUserBean): Boolean = {
    true
  }

  //FIXME
  def isRoot(user: org.beangle.security.blueprint.User): Boolean = {
    false
  }

  def create(creator: UrpUserBean, user: UrpUserBean): Unit = {
//    user.creator = creator
    user.updatedAt = new ju.Date
    entityDao.saveOrUpdate(user)
    //    publish(new UserCreationEvent(Collections.singletonList(newUser)));
  }

  def updateState(manager: UrpUserBean, userIds: Array[java.lang.Long], active: Boolean): Int = {
    0
  }
  def remove(manager: UrpUserBean, user: UrpUserBean): Unit = {
    val removed = new collection.mutable.ListBuffer[User]
    if (isManagedBy(manager, user)) {
      entityDao.remove(entityDao.findBy(classOf[UserProfileBean], "user", List(user)), user);
      removed += user
    }
    //    entityDao.remove(removed)
    //    publish(new UserRemoveEvent(removed));
  }
}