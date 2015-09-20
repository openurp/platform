package org.openurp.platform.security.service.impl

import org.openurp.platform.security.model.Role
import org.openurp.platform.security.service.RoleManager
import org.openurp.platform.security.model.User

class RoleManagerImpl extends RoleManager {

  override def isManagedBy(manager: User, role: Role): Boolean = {
    true
  }

  override def create(creator: User, role: Role): Unit = {

  }

  override def move(role: Role, parent: Role, indexno: Int): Unit = {

  }
  //FIXME
  override def remove(manager: User, roles: Seq[Role]): Unit = {

  }
}