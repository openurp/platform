/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.platform.security.service

import org.openurp.platform.security.model.FuncResource
import org.openurp.platform.user.model.{Dimension, Profile, User, UserProfile}

trait ProfileService {

  def getProfiles(user: User, resource: FuncResource): collection.Seq[Profile]

  def getProfiles(usercode: String): Seq[UserProfile]

  def getDimensionValues(field: Dimension, keys: String*): collection.Seq[Any]

  def getDimension(fieldName: String): Dimension

  def get(id: java.lang.Long): Profile

}
