package org.openurp.platform.security.action

import org.beangle.data.dao.OqlBuilder
import org.beangle.data.model.Entity
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.kernel.service.AppDataPermissionManager
import org.openurp.platform.security.model.DataResource

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

  protected override def saveAndRedirect(resource: DataResource): View = {
    if (null != resource) {
      if (entityDao.duplicate(classOf[DataResource], resource.id, "name", resource.name)) {
        return redirect("edit", "error.notUnique");
      }
    }
    entityDao.saveOrUpdate(resource)
    redirect("search", "info.save.success")
  }

  override def info(id: String): String = {
    val entity = getModel[Entity[_]](entityName, id)
//    val roleQuery = OqlBuilder.from(classOf[AppDataPermission], "auth")
//    roleQuery.where("auth.resource=:resource", entity).select("auth.role")
    put(shortName, entity)
//    put("roles", entityDao.search(roleQuery))
    return forward()
  }

  protected override def shortName: String = {
    "resource"
  }

}
