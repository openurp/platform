
package org.openurp.platform.security.action

import org.beangle.commons.collection.Collections
import org.beangle.data.jpa.dao.OqlBuilder
import org.beangle.data.model.util.Hierarchicals
import org.beangle.webmvc.api.annotation.ignore
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.security.model.{ FuncResource, Menu, MenuProfile }
import org.openurp.platform.security.service.MenuService
import org.openurp.platform.security.model.FuncPermission
import org.openurp.platform.security.model.Menu
import org.beangle.webmvc.api.annotation.param
import org.openurp.platform.security.model.FuncPermission

class MenuAction(val menuService: MenuService) extends RestfulAction[Menu] {

  protected override def indexSetting(): Unit = {
    put("profiles", entityDao.getAll(classOf[MenuProfile]))
  }

  protected override def editSetting(menu: Menu): Unit = {
    put("profiles", entityDao.getAll(classOf[MenuProfile]))
    var folders = Collections.newBuffer[Menu]
    val builder = OqlBuilder.from(classOf[FuncResource], "r")
    if (null != menu.profile && null != menu.profile.id) {
      val profile = entityDao.get(classOf[MenuProfile], menu.profile.id)
      // 查找可以作为父节点的菜单
      val folderBuilder = OqlBuilder.from(classOf[Menu], "m")
      folderBuilder.where("m.entry is null and m.profile=:profile", profile)
      folderBuilder.orderBy("m.indexno")
      val rs = entityDao.search(folderBuilder)
      folders ++= rs
      if (null != menu.parent && !folders.contains(menu.parent)) folders += menu.parent
      folders --= Hierarchicals.getFamily(menu)
    }
    val resources = Collections.newBuffer[FuncResource]
    resources ++= entityDao.search(builder)
    resources --= menu.resources
    put("parents", folders)
    put("resources", resources)
  }

  protected override def removeAndRedirect(menus: Seq[Menu]): View = {
    val parents = Collections.newBuffer[Menu]
    for (menu <- menus) {
      if (null != menu.parent) {
        menu.parent.children -= menu
        parents += menu.parent
      }
    }
    entityDao.saveOrUpdate(parents)
    return super.removeAndRedirect(menus)
  }

  protected override def saveAndRedirect(menu: Menu): View = {
    val resources = entityDao.find(classOf[FuncResource], getAll("resourceId", classOf[Integer]))
    menu.resources.clear()
    menu.resources ++= resources
    val newParentId = get("parent.id", classOf[Integer])
    val indexno = getInt("indexno", 0)
    var parent: Menu = null
    if (None != newParentId) parent = entityDao.get(classOf[Menu], newParentId.get)

    //menuService.move(menu, parent, indexno)
    entityDao.saveOrUpdate(menu)
    if (!menu.enabled) {
      val family = Hierarchicals.getFamily(menu)
      for (one <- family) one.enabled = false
      entityDao.saveOrUpdate(family)
    }
    return redirect("search", "info.save.success")
  }

  //  /**
  //   * 禁用或激活一个或多个模块
  //   */
  //   def  activate():View= {
  //    Integer[] menuIds = getIds(getShortName(), Integer.class)
  //    Boolean enabled = getBoolean("isActivate")
  //    if (null == enabled) enabled = Boolean.TRUE
  //
  //    Set<Menu> updated = CollectUtils.newHashSet()
  //    List<Menu> menus = entityDao.get(classOf[Menu], menuIds)
  //    for (Menu menu : menus) {
  //      if (enabled) {
  //        updated.addAll(HierarchyEntityUtils.getPath(menu))
  //      } else {
  //        updated.addAll(HierarchyEntityUtils.getFamily(menu))
  //      }
  //    }
  //    for (Menu menu : updated)
  //      ((MenuBean) menu).setEnabled(enabled)
  //    entityDao.saveOrUpdate(updated)
  //
  //    return redirect("search", "info.save.success")
  //  }
  //
  //  /**
  //   * 打印预览功能列表
  //   */
  //   String preview() {
  //    OqlBuilder<Menu> query = OqlBuilder.from(classOf[Menu], "menu")
  //    populateConditions(query)
  //    query.orderBy("menu.indexno asc")
  //    put("menus", entityDao.search(query))
  //
  //    query.clearOrders()
  //    query.select("max(length(menu.indexno)/2)")
  //    List<?> rs = entityDao.search(query)
  //    put("depth", rs.get(0))
  //    return forward()
  //  }
  //
  override def info(@param("id") id: String): String = {
    val menu = this.entityDao.get(classOf[Menu], Integer.valueOf(id))
    put("menu", menu)
    if (!menu.resources.isEmpty) {
      val roleQuery = OqlBuilder.from(classOf[FuncPermission], "auth")
      roleQuery.where("auth.resource in(:resources)", menu.resources).select("distinct auth.role")
      put("roles", entityDao.search(roleQuery))
    }
    return forward()
  }
  //
  //   String xml() {
  //    put("resources", entityDao.getAll(FuncResource.class))
  //    put("menuProfiles", entityDao.getAll(MenuProfile.class))
  //    return forward()
  //  }

}