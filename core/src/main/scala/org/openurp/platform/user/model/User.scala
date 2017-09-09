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
package org.openurp.platform.user.model

import java.security.Principal
import java.time.LocalDate
import java.{ util => ju }

import org.beangle.commons.collection.Collections
import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.{ Coded, Enabled, Named, Remark, TemporalOn, Updated }

/**
 * @author chaostone
 */

class User extends LongId with Coded with Named with Updated with TemporalOn with Enabled with Profile with Principal with Remark {
  var locked: Boolean = _
  var password: String = _
  var passwordExpiredOn: Option[ju.Date] = None
  var roles = Collections.newBuffer[RoleMember]
  var groups = Collections.newBuffer[GroupMember]
  var properties = Collections.newMap[Dimension, String]
  var category: UserCategory = _

  def credential = password

  def accountExpired: Boolean = {
    endOn match {
      case Some(e) => LocalDate.now.isAfter(e)
      case None    => false
    }
  }

  def credentialExpired: Boolean = {
    if (None == passwordExpiredOn) false
    else (new ju.Date).after(passwordExpiredOn.get)
  }

  override def getName: String = {
    name
  }
}