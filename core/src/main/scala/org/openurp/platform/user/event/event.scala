/*
 * Beangle, Agile Development Scaffold and Toolkit
 *
 * Copyright (c) 2005-2017, Beangle Software.
 *
 * Beangle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beangle is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Beangle.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.platform.user.event

import org.beangle.commons.event.Event
import org.openurp.platform.user.model.Role
import org.openurp.platform.user.model.User

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