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

import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.openurp.platform.config.model.Credential
import org.openurp.platform.config.service.{CredentialService, DomainService}

class CredentialServiceImpl(entityDao: EntityDao) extends CredentialService {

  var domainService: DomainService = _

  override def getAll(): Seq[Credential] = {
    val query = OqlBuilder.from(classOf[Credential], "o")
    query.where("o.domain=:domain", domainService.getDomain)
    entityDao.search(query)
  }

}
