package org.openurp.platform.security.service.impl

import java.{ util => ju }
import org.beangle.data.dao.EntityDao
import org.openurp.platform.security.model.{ User, UserProfile }
import org.openurp.platform.security.model.Member
import org.openurp.platform.security.model.MemberShip
import org.openurp.platform.security.model.MemberShip.{ Granter, Manager, Member, Ship }
import org.openurp.platform.security.service.UserManager
import org.beangle.data.dao.OqlBuilder
import org.openurp.platform.security.model.Root
import org.openurp.platform.kernel.model.App
import org.openurp.platform.security.model.Role

class UserManagerImpl(val entityDao: EntityDao) extends UserManager {

  def get(code: String): Option[User] = {
    val rs = entityDao.findBy(classOf[User], "code", List(code))
    if (rs.isEmpty) None else Some(rs.head)
  }

  def get(id: Long): User = {
    entityDao.get(classOf[User], id)
  }

  def getUsers(ids: Long*): Seq[User] = {
    entityDao.find(classOf[User], ids.toList)
  }

  import MemberShip._
  def getMembers(user: User, ship: Ship): Seq[Member] = {
    //    if (isRoot(user, app.name)) {
    //      val members = entityDao.search(OqlBuilder.from(classOf[Role], "r").map(r => new Member(user, r))
    //      members.foreach { m =>
    //        m.is(Manager)
    //        m.is(Granter)
    //      }
    //      members
    //    } else {
    ship match {
      case Manager => user.members.filter(m => m.manager)
      case Granter => user.members.filter(m => m.granter)
      case Member => user.members.filter(m => m.member)
      //      }
    }
  }

  //FIXME
  def isManagedBy(manager: User, user: User): Boolean = {
    true
  }

  def isRoot(user: User, appName: String): Boolean = {
    !entityDao.search(OqlBuilder.from(classOf[Root], "r").where("r.user=:user and r.app.name=:appName", user, appName)).isEmpty
  }

  def create(creator: User, user: User): Unit = {
    //    user.creator = creator
    user.updatedAt = new ju.Date
    entityDao.saveOrUpdate(user)
    //    publish(new UserCreationEvent(Collections.singletonList(newUser)));
  }

  def updateState(manager: User, userIds: Iterable[Long], active: Boolean): Int = {
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