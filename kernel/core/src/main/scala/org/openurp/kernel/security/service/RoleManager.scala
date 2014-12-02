package org.openurp.kernel.security.service

import org.beangle.security.blueprint.{ Role, User }
import org.beangle.security.blueprint.service.RoleService
import org.openurp.kernel.security.model.UrpRoleBean

trait RoleManager extends RoleService {

  def isManagedBy(manager: User, role: UrpRoleBean): Boolean

  def create(creator: User, role: UrpRoleBean): Unit

  def move(role: Role, parent: Role, indexno: Int): Unit

  def remove(manager: User, roles: Seq[UrpRoleBean]): Unit

  def get(id: Integer): Role = {
    null
  }

}