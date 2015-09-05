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

import org.beangle.commons.lang.{ Numbers, Strings }
import org.beangle.data.jpa.dao.OqlBuilder
import org.beangle.webmvc.api.annotation.{ mapping, param }
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.api.security.Securities
import org.openurp.platform.security.model.{ FuncPermission, FuncResource, Menu, MenuProfile, Role, User }
import org.openurp.platform.security.service.{ FuncPermissionManager, MenuService }

/**
 * 权限分配与管理响应类
 *
 * @author chaostone 2005-10-9
 */
class PermissionAction extends RestfulAction[FuncPermission] {

  var menuService: MenuService = _
  var funcPermissionManager: FuncPermissionManager = _

  /**
   * 根据菜单配置来分配权限
   */
  @mapping(value = "{role.id}/edit")
  override def edit(@param("role.id") id: String): String = {
    val roleId = Numbers.convert2Int(id)
    val role = entityDao.get(classOf[Role], roleId)
    val user = entityDao.get(classOf[User], Securities.loginUserId).asInstanceOf[User]
    put("manager", user)
    val mngRoles = new collection.mutable.ListBuffer[Role]
    for (m <- user.members) {
      if (m.granter) mngRoles += m.role
    }
    put("mngRoles", mngRoles)
    val menuProfiles = menuService.getProfiles(role)
    put("menuProfiles", menuProfiles)

    var menuProfile = menuService.getProfile(role, getIntId("menuProfile"))
    if (null == menuProfile && !menuProfiles.isEmpty) {
      menuProfile = menuProfiles(0)
    }
    var menus = new collection.mutable.ListBuffer[Menu]
    if (null != menuProfile) {
      var resources: collection.Seq[Object] = null
      if (Securities.isAdmin) {
        menus ++= menuProfile.menus
        resources = entityDao.getAll(classOf[FuncResource])
      } else {
        resources = new collection.mutable.ListBuffer[FuncResource]
        val params = new collection.mutable.HashMap[String, Object]
        val hql = "select distinct fp.resource from " + classOf[FuncPermission].getName + " fp where fp.role.id = :roleId"
        val menuSet = new collection.mutable.HashSet[Menu]
        for (m <- user.members) {
          if (m.granter) {
            menuSet ++= menuService.getMenus(menuProfile, m.role, Some(true))
            params.put("roleId", m.role.id)
            resources ++= entityDao.search(OqlBuilder.oql[FuncResource](hql).params(params))
          }
        }
        menus ++= menuSet.toList.sorted
      }
      put("resources", resources.toSet)
      val displayFreezen = getBoolean("displayFreezen", false)
      if (!displayFreezen) {
        val freezed = new collection.mutable.ListBuffer[Menu]
        for (menu <- menus) if (!menu.enabled) freezed += menu
        menus --= freezed
      }
      val permissions = funcPermissionManager.getPermissions(role)
      val roleMenus = menuService.getMenus(menuProfile, role, None)
      val roleResources = permissions.map(p => p.resource).toSet
      put("roleMenus", roleMenus.toSet)
      put("roleResources", roleResources)

      val parents = new collection.mutable.HashSet[Role]
      val parentResources = new collection.mutable.HashSet[FuncResource]
      val parentMenus = new collection.mutable.HashSet[Menu]
      var parent = role.parent
      while (null != parent && !parents.contains(parent)) {
        val parentPermissions = funcPermissionManager.getPermissions(parent)
        parentMenus ++= menuService.getMenus(menuProfile, parent, null)
        for (permission <- parentPermissions) {
          parentResources += permission.resource
        }
        parents += parent
        parent = parent.parent
      }
      put("parentMenus", parentMenus)
      put("parentResources", parentResources)
    } else {
      put("roleMenus", Set.empty)
      put("roleResources", Set.empty)
      put("parentMenus", Set.empty)
      put("parentResources", Set.empty)
    }
    put("menus", menus)
    put("menuProfile", menuProfile)
    put("role", role)
    return forward()
  }

  /**
   * 显示权限操作提示界面
   */
  def prompt(): String = {
    forward()
  }

  /**
   * 保存模块级权限
   */
  override def save(): View = {
    val role = entityDao.get(classOf[Role], getIntId("role"))
    val menuProfile = entityDao.get(classOf[MenuProfile], getIntId("menuProfile"))
    val newResources = entityDao.findBy(classOf[FuncResource], "id", Strings.split(get("resourceId", "")).map(a => Integer.valueOf(a))).toSet

    // 管理员拥有的菜单权限和系统资源
    val manager = entityDao.get(classOf[User], Securities.loginUserId)
    var mngMenus: collection.Set[Menu] = null
    val mngResources = new collection.mutable.HashSet[FuncResource]
    if (Securities.isAdmin) {
      mngMenus = menuProfile.menus.toSet
    } else {
      mngMenus = menuService.getMenus(menuProfile, manager, manager.profiles).toSet
    }
    for (m <- mngMenus) {
      mngResources ++= m.resources
    }

    //newResources.retainAll(mngResources)
    newResources.dropWhile(p => !mngResources.contains(p))
    funcPermissionManager.authorize(role, newResources)

    val where = to(this, "edit")
    where.param("role.id", role.id).param("menuProfileId", menuProfile.id)
    val displayFreezen = get("displayFreezen")
    if (null != displayFreezen) where.param("displayFreezen", displayFreezen)

    redirect(where, "info.save.success")
  }
}