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
package org.openurp.platform.admin.action.session

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.annotation.ignore
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.config.service.DomainService
import org.openurp.platform.session.model.SessionConfig
import org.openurp.platform.user.service.UserService

class ConfigAction extends RestfulAction[SessionConfig] {

  var domainService: DomainService = _

  var userService: UserService = _

  protected override def editSetting(resource: SessionConfig): Unit = {
    put("categories", userService.getCategories())
  }

  @ignore
  override protected def saveAndRedirect(config: SessionConfig): View = {
    config.domain = domainService.getDomain
    super.saveAndRedirect(config)
  }

  override protected def getQueryBuilder: OqlBuilder[SessionConfig] = {
    super.getQueryBuilder.where("sessionConfig.domain=:domain", domainService.getDomain)
  }
}
