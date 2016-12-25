package org.openurp.platform.user.service.impl

import java.util.Date

import org.beangle.commons.dao.{ EntityDao, OqlBuilder }
import org.beangle.commons.model.util.Hierarchicals
import org.openurp.platform.user.model.{ Role, User }
import org.openurp.platform.user.service.RoleService

class RoleServiceImpl extends RoleService {

  var entityDao: EntityDao = _
  override def isManagedBy(manager: User, role: Role): Boolean = {
    true
  }

  override def create(creator: User, role: Role): Unit = {
    role.creator = creator
    role.updatedAt = new Date()
    entityDao.saveOrUpdate(role)
  }

  override def move(role: Role, parent: Role, indexno: Int): Unit = {
    val nodes =
      if (null != parent) Hierarchicals.move(role, parent, indexno)
      else {
        val builder = OqlBuilder.from(classOf[Role], "r")
          .where("r.parent is null")
          .orderBy("r.indexno")
        Hierarchicals.move(role, entityDao.search(builder).toBuffer, indexno)
      }
    entityDao.saveOrUpdate(nodes)
  }

  override def remove(manager: User, roles: Seq[Role]): Unit = {
    entityDao.remove(roles)
  }
}