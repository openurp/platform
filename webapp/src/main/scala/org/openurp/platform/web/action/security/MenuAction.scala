
package org.openurp.platform.web.action.security

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.model.util.Hierarchicals
import org.beangle.webmvc.api.annotation.{ ignore, param }
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.config.model.App
import org.openurp.platform.config.service.AppService
import org.openurp.platform.security.model.{ FuncPermission, FuncResource, Menu }
import org.openurp.platform.security.service.MenuService
import org.openurp.platform.web.helper.AppHelper

class MenuAction extends RestfulAction[Menu] {
  var menuService: MenuService = _
  var appService: AppService = _

  protected override def indexSetting(): Unit = {
    var apps = appService.getWebapps()
    AppHelper.putApps(apps, "menu.app.id", entityDao)
  }

  override def search(): String = {
    AppHelper.remember("menu.app.id")
    super.search()
    forward()
  }

  protected override def editSetting(menu: Menu): Unit = {
    //search profile in app scope
    val app = entityDao.get(classOf[App], menu.app.id);

    var folders = Collections.newBuffer[Menu]

    // 查找可以作为父节点的菜单
    val folderBuilder = OqlBuilder.from(classOf[Menu], "m")
    folderBuilder.where("m.entry is null and m.app=:app", app)
    folderBuilder.orderBy("m.indexno")
    val rs = entityDao.search(folderBuilder)
    folders ++= rs
    menu.parent foreach { p =>
      if (!folders.contains(p)) folders += p
    }
    folders --= Hierarchicals.getFamily(menu)
    put("parents", folders)

    val alternatives = Collections.newBuffer[FuncResource]
    val resources = Collections.newBuffer[FuncResource]
    val funcBuilder = OqlBuilder.from(classOf[FuncResource], "r").where("r.app=:app", app)
    alternatives ++= entityDao.search(funcBuilder)
    resources ++= alternatives
    alternatives --= menu.resources
    put("alternatives", alternatives)
    put("resources", resources)
  }

  @ignore
  protected override def removeAndRedirect(entities: Seq[Menu]): View = {
    val parents = Collections.newBuffer[Menu]
    for (menu <- entities) {
      menu.parent foreach { p =>
        p.children -= menu
        parents += p
      }
    }
    entityDao.saveOrUpdate(parents)
    super.removeAndRedirect(entities)
  }

  @ignore
  protected override def saveAndRedirect(menu: Menu): View = {
    val resources = entityDao.find(classOf[FuncResource], intIds("resource"))
    menu.resources.clear()
    menu.resources ++= resources
    val newParentId = getInt("parent.id")
    val indexno = getInt("indexno", 0)
    var parent: Menu = null
    if (None != newParentId) parent = entityDao.get(classOf[Menu], newParentId.get)

    menuService.move(menu, parent, indexno)
    if (!menu.enabled) {
      val family = Hierarchicals.getFamily(menu)
      for (one <- family) one.enabled = false
      entityDao.saveOrUpdate(family)
    }
    return redirect("search", "info.save.success")
  }

  /**
   * 禁用或激活一个或多个模块
   */
  def activate(): View = {
    val menuIds = intIds("menu")
    val enabled = getBoolean("isActivate", true)

    val updated = Collections.newSet[Menu]
    val menus = entityDao.find(classOf[Menu], menuIds)
    for (menu <- menus) {
      updated ++= (if (enabled) Hierarchicals.getPath(menu) else Hierarchicals.getFamily(menu))
    }
    for (menu <- updated) menu.enabled = enabled
    entityDao.saveOrUpdate(updated)
    return redirect("search", "info.save.success")
  }

  override def info(@param("id") id: String): String = {
    val menu = this.entityDao.get(classOf[Menu], Integer.parseInt(id))
    put("menu", menu)
    if (!menu.resources.isEmpty) {
      val roleQuery = OqlBuilder.from(classOf[FuncPermission], "auth")
      roleQuery.where("auth.resource in(:resources)", menu.resources).select("distinct auth.role")
      put("roles", entityDao.search(roleQuery))
    }
    return forward()
  }

}
