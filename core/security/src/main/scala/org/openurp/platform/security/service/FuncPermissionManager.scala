package org.openurp.platform.security.service

import org.openurp.platform.config.model.App
import org.openurp.platform.security.model.{ FuncPermission, FuncResource }
import org.openurp.platform.user.model.{ Role, User }

trait FuncPermissionService {

  def getResource(app: App, name: String): Option[FuncResource]

  def getResourceIdsByRole(roleId: Int): Set[Int]

  def getResources(user: User): Seq[FuncResource]

  def getResources(app: App): Seq[FuncResource]

  def getPermissions(app: App, role: Role): Seq[FuncPermission]

  def activate(resourceId: Iterable[Int], active: Boolean): Unit

  def authorize(app: App, role: Role, resources: Set[FuncResource])

}
