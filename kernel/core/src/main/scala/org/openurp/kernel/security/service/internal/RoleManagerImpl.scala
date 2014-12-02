package org.openurp.kernel.security.service.internal

import org.openurp.kernel.security.model.UrpRoleBean
import org.beangle.security.blueprint.Role
import org.beangle.security.blueprint.User
import org.openurp.kernel.security.service.RoleManager

class RoleManagerImpl extends RoleManager {
  
  override def isManagedBy(manager: User, role: UrpRoleBean): Boolean = {
    true
  }

  override def create(creator: User, role: UrpRoleBean): Unit = {

  }

  override def move(role: Role, parent: Role, indexno: Int): Unit = {

  }

  override def remove(manager: User, roles: Seq[UrpRoleBean]): Unit = {

  }
}