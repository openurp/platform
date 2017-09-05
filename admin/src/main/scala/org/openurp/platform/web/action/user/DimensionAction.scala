/*
 * Beangle, Agile Development Scaffold and Toolkit
 *
 * Copyright (c) 2005-2014, Beangle Software.
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
package org.openurp.platform.web.action.user

import org.beangle.webmvc.api.annotation.ignore
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.user.model.Dimension
import org.openurp.platform.config.model.Domain

/**
 * 数据限制域元信息配置类
 *
 * @author chaostone
 */
class DimensionAction extends RestfulAction[Dimension] {

  override def editSetting(dimension: Dimension): Unit = {
    val domains = entityDao.getAll(classOf[Domain]).toBuffer -- dimension.domains
    put("domains", domains)
  }
  protected override def saveAndRedirect(field: Dimension): View = {
    if (entityDao.duplicate(classOf[Dimension], field.id, "name", field.name)) {
      addError("名称重复")
      return forward(to(this, "edit"))
    }
    field.domains = new collection.mutable.ListBuffer[Domain]
    val domainId2nd = getAll("domainId2nd", classOf[Int])
    field.domains ++= entityDao.find(classOf[Domain], domainId2nd)
    entityDao.saveOrUpdate(field)
    redirect("search", "info.save.success")
  }
}
