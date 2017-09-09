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
package org.openurp.platform.security.model

import java.security.Principal
import java.time.Instant

import org.beangle.data.model.{ IntId, LongId }
import org.beangle.data.model.pojo.{ Named, Remark }
import org.beangle.security.authz.{ Permission, Resource, Scopes }
import org.openurp.platform.config.model.{ App, Domain }
import org.openurp.platform.user.model.Role

class DataPermission extends LongId  with Permission with Remark {
  var domain: Domain = _
  var app: Option[App] = None
  var resource: DataResource = _
  var description: String = _
  var filters: String = _
  var funcResource: Option[FuncResource] = None
  var attrs: Option[String] = None
  var actions: Option[String] = None
  var restrictions: Option[String] = None
  var role: Option[Role] = None

  var beginAt:Instant=_
  var endAt:Option[Instant]=None
  def principal: Principal = role.orNull
}

class DataResource extends IntId with Named with Resource with Remark {
  var domain: Domain = _
  var scope = Scopes.Public
  var typeName: String = _
  var title: String = _
  var actions: Option[String] = None
  def enabled = true
}
