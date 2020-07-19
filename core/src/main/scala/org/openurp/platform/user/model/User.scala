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
package org.openurp.platform.user.model

import java.security.Principal
import java.time.LocalDate
import java.{util => ju}

import org.beangle.commons.collection.Collections
import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.{Coded, Enabled, Named, Remark, TemporalOn, Updated}
import org.openurp.platform.config.model.Org

/**
 * @author chaostone
 */

class User extends LongId with Coded with Named with Updated with TemporalOn with Profile with Principal with Remark {
  var org: Org = _
  var roles = Collections.newBuffer[RoleMember]
  var groups = Collections.newBuffer[GroupMember]
  var properties = Collections.newMap[Dimension, String]
  var acounts = Collections.newBuffer[Account]
  var category: UserCategory = _
  var avatarId: Option[String] = None

  override def getName: String = {
    name
  }
}
