package org.openurp.platform.security.action

import org.beangle.data.jpa.dao.OqlBuilder
import org.beangle.data.model.Entity
import org.beangle.webmvc.api.annotation.ignore
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.kernel.model.App
import org.openurp.platform.security.model.{ FuncPermission, FuncResource, Menu }
import org.openurp.platform.security.service.FuncPermissionManager
import org.openurp.platform.security.helper.AppHelper

/**
 * 系统模块管理响应类
 *
 * @author chaostone 2005-10-9
 */
class FuncResourceAction extends RestfulAction[FuncResource] {

  var funcPermissionManager: FuncPermissionManager = _
  /**
   * 禁用或激活一个或多个模块
   */
  def activate(): View = {
    val resourceIds = intIds("resource")
    val enabled = getBoolean("enabled", false)
    funcPermissionManager.activate(resourceIds, enabled.booleanValue())
    return redirect("search", "info.save.success")
  }

  protected override def saveAndRedirect(resource: FuncResource): View = {
    if (null != resource) {
      if (entityDao.duplicate(classOf[FuncResource], resource.id, "name", resource.name)) {
        return redirect("edit", "error.notUnique");
      }
    }
    entityDao.saveOrUpdate(resource)
    redirect("search", "info.save.success")
  }

  override def info(id: String): String = {
    val entity = getModel[Entity[_]](entityName, id)
    val query = OqlBuilder.from(classOf[Menu], "menu")
    query.join("menu.resources", "r").where("r.id=:resourceId", entity.id)
      .orderBy("menu.profile.id,menu.indexno")

    val roleQuery = OqlBuilder.from(classOf[FuncPermission], "auth")
    roleQuery.where("auth.resource=:resource", entity).select("auth.role")
    put(shortName, entity)
    put("roles", entityDao.search(roleQuery))
    put("menus", entityDao.search(query))
    return forward()
  }

  protected override def editSetting(entity: FuncResource): Unit = {
    put("apps", AppHelper.getApps(entityDao))
  }
  //  protected PropertyExtractor getPropertyExtractor() {
  //    return new ResourcePropertyExtractor(getTextResource())
  //  }

  protected override def shortName: String = {
    "resource"
  }

  protected override def indexSetting(): Unit = {
    val rs = populate(entityName, "resource")
    put("apps", AppHelper.getApps(entityDao))
  }

}
