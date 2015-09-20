package org.openurp.platform.security.service

import org.openurp.platform.security.model.{ FuncPermission, FuncResource, Role, User }

trait FuncPermissionService {

  def getResource(name: String): Option[FuncResource]

  def getResourceNamesByRole(roleId: Integer): Set[Integer]

  def getResources(user: User): Seq[FuncResource]

  def getPermissions(role: Role): Seq[FuncPermission]
}