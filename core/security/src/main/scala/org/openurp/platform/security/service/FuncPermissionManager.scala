package org.openurp.platform.security.service

import org.openurp.platform.security.model.FuncResource
import org.openurp.platform.user.model.Role
import org.openurp.platform.security.model.FuncPermission
import org.openurp.platform.user.model.User

trait FuncPermissionService {

  def getResource(name: String): Option[FuncResource]

  def getResourceNamesByRole(roleId: Integer): Set[Integer]

  def getResources(user: User): Seq[FuncResource]

  def getPermissions(role: Role): Seq[FuncPermission]

  def activate(resourceId: Iterable[Int], active: Boolean): Unit

  def authorize(role: Role, resources: Set[FuncResource])

}