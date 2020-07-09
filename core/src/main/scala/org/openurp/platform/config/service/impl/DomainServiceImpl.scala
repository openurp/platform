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
package org.openurp.platform.config.service.impl

import org.beangle.commons.bean.Initializing
import org.beangle.data.dao.EntityDao
import org.openurp.app.Urp
import org.openurp.platform.config.model.Domain

class DomainServiceImpl extends DomainService with Initializing {
  var entityDao: EntityDao = _

  var domain: Domain = _

  override def getDomain: Domain = {
    domain
  }

  override def init(): Unit = {
    val rs = entityDao.findBy(classOf[Domain], "hostname", List(Urp.hostname))
    rs.headOption match {
      case Some(d) => domain = d
      case None => throw new RuntimeException("Cannot find domain with hostname :" + Urp.hostname)
    }
  }
}
