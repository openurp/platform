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
package org.openurp.platform.ws.config

import org.beangle.data.dao.EntityDao
import org.beangle.webmvc.api.action.ActionSupport
import org.openurp.platform.config.model.Org
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.api.annotation.response
import org.beangle.commons.collection.Properties

class OrgWS(entityDao: EntityDao) extends ActionSupport {

  @response
  def index(): Properties = {
    val orgs = entityDao.getAll(classOf[Org])
    if (orgs.isEmpty) {
      new Properties()
    } else {
      new Properties(orgs.head, "id", "code", "name", "shortName", "logoUrl", "wwwUrl")
    }
  }
}
