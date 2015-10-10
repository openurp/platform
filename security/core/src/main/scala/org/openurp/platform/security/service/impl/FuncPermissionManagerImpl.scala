package org.openurp.platform.security.service.impl

import org.beangle.data.dao.OqlBuilder
import org.beangle.data.dao.EntityDao
import org.openurp.platform.security.model.{ FuncPermission, FuncResource, Role, User }
import org.openurp.platform.security.service.FuncPermissionManager
import org.openurp.platform.api.app.AppConfig

class FuncPermissionManagerImpl(val entityDao: EntityDao) extends FuncPermissionManager {

  def getResource(name: String): Option[FuncResource] = {
    val query = OqlBuilder.from(classOf[FuncResource], "r")
    query.where("r.name=:name and r.app.name=:appName", name, AppConfig.name).cacheable()
    val rs = entityDao.search(query)
    if (rs.isEmpty) None else Some(rs.head)
  }

  def getResourceNamesByRole(roleId: Integer): Set[Integer] = {
    val hql = "select a.resource.id from " + classOf[FuncPermission].getName() +
      " as a where a.role.id= :roleId and a.resource.enabled = true"
    val query = OqlBuilder.oql[FuncPermission](hql).param("roleId", roleId).cacheable()
    entityDao.search(query).toSet.asInstanceOf[Set[Integer]]
  }

  def getResources(user: User): Seq[FuncResource] = {
    null
  }

  def getPermissions(role: Role): Seq[FuncPermission] = {
    null
  }

  def activate(resourceId: Iterable[Int], active: Boolean): Unit = {

  }

  def authorize(role: Role, resources: Set[FuncResource]): Unit = {

  }
}