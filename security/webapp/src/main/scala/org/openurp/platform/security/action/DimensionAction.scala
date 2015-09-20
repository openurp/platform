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
package org.openurp.platform.security.action

import org.beangle.webmvc.api.annotation.ignore
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.kernel.model.App
import org.openurp.platform.security.model.Dimension

/**
 * 数据限制域元信息配置类
 *
 * @author chaostone
 */
class DimensionAction extends RestfulAction[Dimension] {

  override def editSetting(dimension: Dimension): Unit = {
    val apps = entityDao.getAll(classOf[App]).toBuffer -- dimension.apps
    put("apps", apps)
  }
  protected override def saveAndRedirect(field: Dimension): View = {
    if (entityDao.duplicate(classOf[Dimension], field.id, "name", field.name)) {
      addError("名称重复")
      return forward(to(this, "edit"))
    }
    field.apps.clear()
    val appId2nd = getAll("appId2nd", classOf[Int])
    field.apps ++= entityDao.find(classOf[App], appId2nd)
    entityDao.saveOrUpdate(field)
    redirect("search", "info.save.success")
  }
}
