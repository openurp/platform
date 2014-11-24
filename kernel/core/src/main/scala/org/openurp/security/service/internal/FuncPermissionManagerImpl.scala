package org.openurp.security.service.internal

import org.beangle.data.model.dao.EntityDao
import org.beangle.data.jpa.dao.OqlBuilder
import org.beangle.security.blueprint.FuncPermission
import org.beangle.security.blueprint.FuncResource
import org.beangle.security.blueprint.Role
import org.openurp.security.service.FuncPermissionManager

class FuncPermissionManagerImpl(val entityDao: EntityDao) extends FuncPermissionManager {

  def getResource(name: String): Option[FuncResource] = {
    val query = OqlBuilder.from(classOf[FuncResource], "r")
    query.where("r.name=:name", name).cacheable()
    val rs = entityDao.search(query)
    if (rs.isEmpty) None else Some(rs.head)
  }

  def getResourceNamesByRole(roleId: Integer): Set[String] = {
    val hql = "select a.resource.name from " + classOf[FuncPermission].getName() +
      " as a where a.role.id= :roleId and a.resource.enabled = true"
    val query = OqlBuilder.oql[FuncPermission](hql).param("roleId", roleId).cacheable()
    entityDao.search(query).toSet.asInstanceOf[Set[String]]
  }

  def getResources(user: org.beangle.security.blueprint.User): Seq[FuncResource] = {
    null
  }

  def getPermissions(role: org.beangle.security.blueprint.Role): Seq[FuncPermission] = {
    null
  }

  def activate(resourceId: Array[Integer], active: Boolean): Unit = {

  }

  def authorize(role: Role, resources: Set[FuncResource]): Unit = {

  }
}