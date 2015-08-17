package org.openurp.platform.security.service

import org.beangle.security.blueprint.{ Role, User }
import org.beangle.security.blueprint.service.RoleService
import org.openurp.platform.security.model.UrpRole

trait RoleManager extends RoleService {

  def isManagedBy(manager: User, role: UrpRole): Boolean

  def create(creator: User, role: UrpRole): Unit

  def move(role: Role, parent: Role, indexno: Int): Unit

  def remove(manager: User, roles: Seq[UrpRole]): Unit

  def get(id: Integer): Role = {
    null
  }

}