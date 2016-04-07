package org.openurp.platform.security.service.impl

import org.beangle.data.dao.OqlBuilder
import org.beangle.data.dao.EntityDao
import org.openurp.platform.api.app.UrpApp
import org.openurp.platform.security.model.FuncPermission
import org.openurp.platform.security.model.FuncResource
import org.openurp.platform.user.model.Role
import org.openurp.platform.user.model.User
import org.openurp.platform.security.service.FuncPermissionService
import org.beangle.commons.collection.Collections
import org.beangle.data.dao.Operation

class FuncPermissionServiceImpl(val entityDao: EntityDao) extends FuncPermissionService {

  def getResource(name: String): Option[FuncResource] = {
    val query = OqlBuilder.from(classOf[FuncResource], "r")
    query.where("r.name=:name and r.app.name=:appName", name, UrpApp.name).cacheable()
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
    entityDao.search(OqlBuilder.from(classOf[FuncPermission], "fp").where("fp.role=:role", role));
  }

  def activate(resourceId: Iterable[Int], active: Boolean): Unit = {

  }

  def authorize(role: Role, resources: Set[FuncResource]): Unit = {
    val resourceSet = Collections.newSet[FuncResource] ++ resources
    val permissions = getPermissions(role).toBuffer
    val builder = new Operation.Builder()
    for (au <- permissions) {
      if (resources.contains(au.resource)) resourceSet.remove(au.resource)
      else builder.remove(au)
    }

    for (resource <- resourceSet) {
      val authority = new FuncPermission(role, resource);
      builder.saveOrUpdate(authority)
    }
    entityDao.execute(builder)
  }
}
