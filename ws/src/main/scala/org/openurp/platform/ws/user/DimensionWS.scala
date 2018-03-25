/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2005, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.platform.ws.user

import org.beangle.commons.collection.Properties
import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.webmvc.api.action.{ ActionSupport, EntitySupport }
import org.beangle.webmvc.api.annotation.{ mapping, param, response }
import org.openurp.platform.user.model.{ RoleMember, User }
import org.openurp.platform.user.service.UserService
import org.openurp.platform.user.model.Dimension

/**
 * @author chaostone
 */
class DimensionWS(entityDao: EntityDao) extends ActionSupport {

  @response
  @mapping("{name}")
  def index(@param("name") name: String): Properties = {
    val query = OqlBuilder.from(classOf[Dimension], "d").where("d.name=:name", name).cacheable()
    val dimensions = entityDao.search(query)
    if (dimensions.isEmpty) new Properties()
    else {
      val dimension = dimensions(0)
      new Properties(dimension, "id", "title", "source", "multiple", "required", "typeName", "keyName", "properties")
    }
  }
}
