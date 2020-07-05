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

import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.config.model.Domain
import org.openurp.platform.user.model.Dimension

/**
 * 数据限制域元信息配置类
 * @author chaostone
 */
class DimensionAction extends RestfulAction[Dimension] {

  override def editSetting(dimension: Dimension): Unit = {

    val domains = entityDao.getAll(classOf[Domain]).toBuffer.subtractAll(dimension.domains)
    put("domains", domains)
  }

  protected override def saveAndRedirect(dimension: Dimension): View = {
    if (entityDao.duplicate(classOf[Dimension], dimension.id, "name", dimension.name)) {
      addError("名称重复")
      return forward(to(this, "edit"))
    }

    dimension.domains = new collection.mutable.ListBuffer[Domain]
    val domainId2nd = getAll("domainId2nd", classOf[Int])
    dimension.domains ++= entityDao.find(classOf[Domain], domainId2nd)
    if(dimension.source.contains("\r")){
      dimension.source=dimension.source.replace("\r","");
    }
    entityDao.saveOrUpdate(dimension)
    redirect("search", "info.save.success")
  }
}
