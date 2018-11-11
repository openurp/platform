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
package org.openurp.platform.admin.action.security

import org.beangle.data.dao.OqlBuilder
import org.beangle.data.model.Entity
import org.beangle.webmvc.api.annotation.ignore
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.admin.helper.AppHelper
import org.openurp.platform.config.service.AppService
import org.openurp.platform.security.model.{ FuncPermission, FuncResource, Menu }
import org.openurp.platform.security.service.FuncPermissionService

/**
 * 系统模块管理响应类
 *
 * @author chaostone 2005-10-9
 */
class FuncResourceAction extends RestfulAction[FuncResource] {

  var funcPermissionService: FuncPermissionService = _
  var appService: AppService = _
  /**
   * 禁用或激活一个或多个模块
   */
  def activate(): View = {
    val resourceIds = intIds("resource")
    val enabled = getBoolean("enabled", false)
    funcPermissionService.activate(resourceIds, enabled)
    return redirect("search", "info.save.success")
  }

  protected override def saveAndRedirect(resource: FuncResource): View = {
    if (null != resource) {
      val builder = OqlBuilder.from[Int](classOf[FuncResource].getName, "fr").where("fr.name=:name and fr.app = :app", resource.name, resource.app).select("fr.id")
      val ids = entityDao.search(builder)
      if (!resource.persisted && !ids.isEmpty || resource.persisted && !ids.isEmpty && !ids.contains(resource.id)) {
        return redirect("edit", "error.notUnique");
      }
    }
    entityDao.saveOrUpdate(resource)
    redirect("search", "info.save.success")
  }

  override def search(): View = {
    AppHelper.remember("resource.app.id")
    super.search()
    forward()
  }

  override def info(id: String): View = {
    val entity = getModel[Entity[_]](entityName, id)
    val query = OqlBuilder.from(classOf[Menu], "menu")
    query.join("menu.resources", "r").where("r.id=:resourceId", entity.id)
      .orderBy("menu.app.id,menu.indexno")

    val roleQuery = OqlBuilder.from(classOf[FuncPermission], "auth")
    roleQuery.where("auth.resource=:resource", entity).select("auth.role")
    put(simpleEntityName, entity)
    put("roles", entityDao.search(roleQuery))
    put("menus", entityDao.search(query))
    return forward()
  }

  protected override def editSetting(entity: FuncResource): Unit = {
    put("apps", appService.getApps())
  }

  protected override def simpleEntityName: String = {
    "resource"
  }

  protected override def indexSetting(): Unit = {
    AppHelper.putApps(appService.getApps(), "resource.app.id", entityDao)
  }

  @ignore
  protected override def removeAndRedirect(entities: Seq[FuncResource]): View = {
    try {
      //删除相关表
      val menuBuilder2 = OqlBuilder.from(classOf[Menu], "m").join("m.resources", "r");
      ("r in(:entries)", entities)
      val menus2 = entityDao.search(menuBuilder2)
      menus2 foreach (m => m.resources --= entities)
      entityDao.saveOrUpdate(menus2)

      //重置依次作为入口的菜单
      val menuBuilder = OqlBuilder.from(classOf[Menu], "m").
        where("m.entry in(:entries)", entities)
      val menus = entityDao.search(menuBuilder)
      menus foreach (m => m.entry = None)

      remove(entities)
      redirect("search", "info.remove.success")
    } catch {
      case e: Exception => {
        logger.info("removeAndForwad failure", e)
        redirect("search", "info.delete.failure")
      }
    }
  }
}
