package org.openurp.platform.user.service.impl

import org.openurp.platform.user.model.{ Role, User }
import org.openurp.platform.user.service.RoleService

class RoleServiceImpl extends RoleService {

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