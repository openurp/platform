package org.openurp.platform.user.service

import org.openurp.platform.user.model.{ Role, User }

trait RoleService   {

  def isManagedBy(manager: User, role: Role): Boolean

  def create(creator: User, role: Role): Unit

  def move(role: Role, parent: Role, indexno: Int): Unit

  def remove(manager: User, roles: Seq[Role]): Unit

  
  def get(id: Integer): Role = {
    null
  }

}