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
package org.openurp.platform.admin.action.user

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.config.service.DomainService
import org.openurp.platform.user.model.Dimension

/**
 * 数据限制域元信息配置类
 * @author chaostone
 */
class DimensionAction extends RestfulAction[Dimension] {

  var domainService: DomainService = _

  override protected def getQueryBuilder: OqlBuilder[Dimension] = {
    val builder = super.getQueryBuilder
    builder.where("dimension.domain=:domain", domainService.getDomain)
    builder
  }

  protected override def saveAndRedirect(dimension: Dimension): View = {
    dimension.domain = domainService.getDomain
    if (dimension.source.contains("\r")) {
      dimension.source = dimension.source.replace("\r", "");
    }
    entityDao.saveOrUpdate(dimension)
    redirect("search", "info.save.success")
  }
}
