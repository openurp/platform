/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.platform.user.service.impl

import java.time.Instant

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.data.model.Entity
import org.openurp.platform.config.service.impl.DomainService
import org.openurp.platform.user.model.MemberShip.{Granter, Manager, Member, Ship}
import org.openurp.platform.user.model._
import org.openurp.platform.user.service.UserService

class UserServiceImpl(val entityDao: EntityDao) extends UserService {

  var domainService: DomainService = _

  def get(code: String): Option[User] = {
    val query = OqlBuilder.from(classOf[User], "u")
    query.where("u.org=:org", domainService.getOrg)
    query.where("u.code=:code", code)
    entityDao.search(query).headOption
  }

  def get(id: Long): User = {
    entityDao.get(classOf[User], id)
  }

  def getUsers(ids: Long*): collection.Seq[User] = {
    entityDao.find(classOf[User], ids.toList)
  }

  def getRoles(user: User, ship: Ship): collection.Seq[RoleMember] = {
    ship match {
      case Manager => user.roles.filter(m => m.manager)
      case Granter => user.roles.filter(m => m.granter)
      case Member => user.roles.filter(m => m.member)
    }
  }

  //FIXME
  def isManagedBy(manager: User, user: User): Boolean = {
    true
  }

  override def isRoot(user: User, appName: String): Boolean = {
    !entityDao.search(OqlBuilder.from(classOf[Root], "r").where("r.user=:user and r.app.name=:appName", user, appName)).isEmpty
  }

  def create(creator: User, user: User): Unit = {
    user.updatedAt = Instant.now
    entityDao.saveOrUpdate(user)
  }

  def updateState(manager: User, userIds: Iterable[Long], active: Boolean): Int = {
    val users = entityDao.find(classOf[User], userIds)
    val updated = users.filter(u => isManagedBy(manager, u))
    updated.foreach { u => u.enabled = active }
    entityDao.saveOrUpdate(updated)
    updated.size
  }

  def remove(manager: User, user: User): Unit = {
    if (isManagedBy(manager, user)) {
      val removed = Collections.newBuffer[Entity[_]]
      removed ++= entityDao.findBy(classOf[Credential], "user", List(user))
      removed ++= entityDao.findBy(classOf[UserProfile], "user", List(user))
      entityDao.remove(removed, user);
    }
  }
}
