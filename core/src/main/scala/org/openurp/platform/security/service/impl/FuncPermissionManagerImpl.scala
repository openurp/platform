/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2005, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.platform.security.service.impl

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.{ EntityDao, Operation, OqlBuilder }
import org.openurp.platform.config.model.App
import org.openurp.platform.security.model.{ FuncPermission, FuncResource }
import org.openurp.platform.security.service.FuncPermissionService
import org.openurp.platform.user.model.{ Role, User }

class FuncPermissionServiceImpl(val entityDao: EntityDao) extends FuncPermissionService {

  def getResource(app: App, name: String): Option[FuncResource] = {
    val query = OqlBuilder.from(classOf[FuncResource], "r")
    query.where("r.name=:name and r.app=:app", name, app).cacheable()
    val rs = entityDao.search(query)
    if (rs.isEmpty) None else Some(rs.head)
  }

  def getResourceIdsByRole(roleId: Int): Set[Int] = {
    val hql = "select a.resource.id from " + classOf[FuncPermission].getName() +
      " as a where a.role.id= :roleId and a.resource.enabled = true"
    val query = OqlBuilder.oql[Int](hql).param("roleId", roleId).cacheable()
    entityDao.search(query).toSet
  }

  def getResources(user: User): Seq[FuncResource] = {
    null
  }

  def getResources(app: App): Seq[FuncResource] = {
    val query = OqlBuilder.from(classOf[FuncResource], "r")
    query.where("r.app = :app", app)
    entityDao.search(query)
  }

  def getPermissions(app: App, role: Role): Seq[FuncPermission] = {
    entityDao.search(OqlBuilder.from(classOf[FuncPermission], "fp").where("fp.resource.app=:app and fp.role=:role", app, role));
  }

  def activate(ids: Iterable[Int], active: Boolean): Unit = {
    val resources = entityDao.find(classOf[FuncResource], ids)
    resources.foreach { f => f.enabled = active }
    entityDao.saveOrUpdate(resources)
  }

  def authorize(app: App, role: Role, resources: Set[FuncResource]): Unit = {
    val resourceSet = Collections.newSet[FuncResource] ++ resources
    val permissions = getPermissions(app, role).toBuffer
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