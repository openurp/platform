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
package org.openurp.platform.ws.security.data

import org.beangle.commons.collection.Properties
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.dao.EntityDao
import org.beangle.webmvc.api.action.{ ActionSupport, EntitySupport }
import org.beangle.webmvc.api.annotation.{ param, response }
import org.openurp.platform.security.model.DataResource

class ResourceWS extends ActionSupport with EntitySupport[DataResource] {

  var entityDao: EntityDao = _

  @response
  def info(@param("name") name: String): Properties = {
    val query = OqlBuilder.from(classOf[DataResource], "fr").where("fr.name=:name", name)
    val resources = entityDao.search(query)
    if (!resources.isEmpty) new Properties(resources.head, "id", "name", "title", "scope")
    else new Properties
  }

}
