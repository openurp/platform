package org.openurp.platform.user.service.impl

import java.util.Date

import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.openurp.platform.user.model.{ Role, User }
import org.openurp.platform.user.service.RoleService
import org.beangle.data.model.util.Hierarchicals
import java.time.ZonedDateTime

class RoleServiceImpl extends RoleService {

  var entityDao: EntityDao = _
  override def isManagedBy(manager: User, role: Role): Boolean = {
    true
  }

  override def create(creator: User, role: Role): Unit = {
    role.creator = creator
    role.updatedAt = ZonedDateTime.now.toInstant
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

  def get(id: Int): Role = {
    entityDao.get(classOf[Role], id)
  }

}