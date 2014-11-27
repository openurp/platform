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
package org.openurp.security.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.beangle.webmvc.api.view.View
import org.beangle.security.blueprint.Field

/**
 * 数据限制域元信息配置类
 *
 * @author chaostone
 */
class FieldAction extends RestfulAction[Field] {

  protected override def saveAndRedirect(field: Field): View = {
    if (entityDao.duplicate(classOf[Field], field.id, "name", field.name)) {
      addError("名称重复")
      return forward(to(this, "edit"))
    }
    entityDao.saveOrUpdate(field)
    redirect("search", "info.save.success")
  }

}
