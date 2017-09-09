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
package org.openurp.platform.user.service

import org.openurp.platform.user.model.MemberShip
import org.openurp.platform.user.model.RoleMember
import org.openurp.platform.user.model.User

trait UserService {

  def get(code: String): Option[User]

  def get(id: Long): User

  def getUsers(id: Long*): Seq[User]

  def getRoles(user: User, ship: MemberShip.Ship): Seq[RoleMember]

  def isManagedBy(manager: User, user: User): Boolean

  def create(creator: User, user: User)

  def updateState(manager: User, userIds: Iterable[Long], active: Boolean): Int

  def remove(creator: User, user: User)

  def isRoot(user: User, appName: String): Boolean
}