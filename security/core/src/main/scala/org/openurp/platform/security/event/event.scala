package org.openurp.platform.security.event

import org.beangle.commons.event.Event
import org.openurp.platform.security.model.{ Role, User }

class RoleEvent(role: Role) extends Event(role) {
  def role = getSource.asInstanceOf[Role]
}

class RolePermissionEvent(role: Role) extends RoleEvent(role)

class RoleCreationEvent(role: Role) extends RoleEvent(role)

class RoleRemoveEvent(role: Role) extends RoleEvent(role)

class UserEvent(user: User) extends Event(user) {
  def user = getSource.asInstanceOf[User]
}

class UserAlterationEvent(user: User) extends UserEvent(user)

class UserCreationEvent(user: User) extends UserEvent(user)

class UserRemoveEvent(user: User) extends UserEvent(user)

class UserStatusEvent(user: User) extends UserEvent(user)