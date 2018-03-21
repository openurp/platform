/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2005, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.platform.web.action.security

import org.beangle.data.dao.OqlBuilder
import org.beangle.data.model.Entity
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.config.service.AppDataPermissionManager
import org.openurp.platform.security.model.DataResource
import org.openurp.platform.config.model.Domain

/**
 * 系统模块管理响应类
 *
 * @author chaostone 2005-10-9
 */
class DataResourceAction extends RestfulAction[DataResource] {

  var appFuncPermissionManager: AppDataPermissionManager = _
  /**
   * 禁用或激活一个或多个模块
   */
  def activate(): View = {
    val resourceIds = intIds("resource")
    val enabled = getBoolean("enabled", false)
    appFuncPermissionManager.activate(resourceIds, enabled.booleanValue())
    return redirect("search", "info.save.success")
  }
  protected override def editSetting(dataPermission: DataResource): Unit = {
    put("domains", entityDao.getAll(classOf[Domain]))
  }

  protected override def saveAndRedirect(resource: DataResource): View = {
    if (null != resource) {
      if (entityDao.duplicate(classOf[DataResource], resource.id, "name", resource.name)) {
        return redirect("edit", "error.notUnique");
      }
    }
    entityDao.saveOrUpdate(resource)
    redirect("search", "info.save.success")
  }

  override def info(id: String): View = {
    val entity = getModel[Entity[_]](entityName, id)
    //    val roleQuery = OqlBuilder.from(classOf[AppDataPermission], "auth")
    //    roleQuery.where("auth.resource=:resource", entity).select("auth.role")
    put(simpleEntityName, entity)
    //    put("roles", entityDao.search(roleQuery))
    return forward()
  }

  protected override def simpleEntityName: String = {
    "resource"
  }

}
