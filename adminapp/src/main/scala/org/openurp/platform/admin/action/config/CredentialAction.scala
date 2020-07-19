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
package org.openurp.platform.admin.action.config

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.annotation.ignore
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.config.model.Credential
import org.openurp.platform.config.service.DomainService

class CredentialAction extends RestfulAction[Credential] {

  var domainService: DomainService = _

  @ignore
  override protected def saveAndRedirect(credential: Credential): View = {
    credential.domain = domainService.getDomain
    super.saveAndRedirect(credential)
  }

  override protected def getQueryBuilder: OqlBuilder[Credential] = {
    val builder = super.getQueryBuilder
    builder.where("credential.domain=:domain", domainService.getDomain)
    builder
  }
}
