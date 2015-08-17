package org.openurp.platform.security.service.internal

import org.openurp.platform.security.model.UrpRole
import org.beangle.security.blueprint.Role
import org.beangle.security.blueprint.User
import org.openurp.platform.security.service.RoleManager

class RoleManagerImpl extends RoleManager {

  override def isManagedBy(manager: User, role: UrpRole): Boolean = {
    true
  }

  override def create(creator: User, role: UrpRole): Unit = {

  }

  override def move(role: Role, parent: Role, indexno: Int): Unit = {

  }
  //FIXME
  override def remove(manager: User, roles: Seq[UrpRole]): Unit = {

  }
}