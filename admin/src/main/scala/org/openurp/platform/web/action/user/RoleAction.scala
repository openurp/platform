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

import java.time.Instant

import org.beangle.data.dao.OqlBuilder
import org.beangle.data.model.util.Hierarchicals
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.app.UrpApp
import org.openurp.app.security.Securities
import org.openurp.platform.config.model.App
import org.openurp.platform.security.service.ProfileService
import org.openurp.platform.user.model.{ Role, User }
import org.openurp.platform.user.service.{ DataResolver, RoleService, UserService }
import org.openurp.platform.user.service.impl.CsvDataResolver
import org.openurp.platform.web.helper.ProfileHelper

/**
 * 角色信息维护响应类
 *
 * @author chaostone 2005-9-29
 */
class RoleAction(val roleService: RoleService, val userService: UserService) extends RestfulAction[Role] {

  var dataResolver: DataResolver = CsvDataResolver

  var profileService: ProfileService = _

  /**
   * 对组可管理意为<br>
   * 1 建立下级组
   * 2 移动下级组顺序
   * 不能改变组的1）权限和2）直接成员，3）删除组，4）重命名，这些将看作组同部分一起看待的。
   * 只要拥有上级组的管理权限，才能变更这些，这些称之为写权限。
   * 成员关系可以等价于读权限
   * 授权关系可以等价于读权限传播
   * 拥有某组的管理权限，不意味拥有下级组的管理权限。新建组情况自动授予该组的其他管理者管理权限。
   */
  override def editSetting(role: Role): Unit = {
    put("role", role)
    val query = OqlBuilder.from(classOf[Role], "role")
    val me = entityDao.findBy(classOf[User], "code", List(Securities.user)).head
    if (!userService.isRoot(me, UrpApp.name)) {
      query.join("role.members", "gm")
      query.where("gm.user=:me and gm.manager=true", me)
    }
    val parents = new collection.mutable.ListBuffer[Role]
    parents ++= entityDao.search(query)
    parents --= Hierarchicals.getFamily(role)
    put("parents", parents)
    forward()
  }

  protected override def getQueryBuilder(): OqlBuilder[Role] = {
    val entityQuery = OqlBuilder.from(classOf[Role], "role")
    val me = entityDao.findBy(classOf[User], "code", List(Securities.user)).head
    //    if (!userService.isRoot(me, app.name)) {
    //      entityQuery.join("role.members", "gm")
    //      entityQuery.where("gm.user=:me and gm.manager=true", me)
    //    }
    populateConditions(entityQuery)
    val orderBy = get("orderBy", "role.indexno")
    entityQuery.limit(getPageLimit).orderBy(orderBy)
  }

  protected override def saveAndRedirect(entity: Role): View = {
    val me = entityDao.findBy(classOf[User], "code", List(Securities.user)).head
    val role = entity.asInstanceOf[Role]
    if (entity.persisted) {
      if (!roleService.isManagedBy(me, role)) {
        return redirect("search", "不能修改该组,你没有" + role.parent.map(p => p.name).orNull + "的管理权限");
      }
    }
    if (entityDao.duplicate(classOf[Role], role.id, "name", role.getName())) return redirect("edit",
      "error.notUnique")
    if (!role.persisted) {
      role.indexno = "tmp"
      roleService.create(me, role)
    } else {
      role.updatedAt = Instant.now
      entityDao.saveOrUpdate(role)
    }
    var parent: Role = null
    val indexno = getInt("indexno", 1)
    getInt("parent.id") match {
      case Some(parentId) => parent = entityDao.get(classOf[Role], parentId)
      case None           =>
    }
    roleService.move(role, parent, indexno)
    if (!role.enabled) {
      val family = Hierarchicals.getFamily(role.asInstanceOf[Role])
      for (one <- family) one.asInstanceOf[Role].enabled = false
      entityDao.saveOrUpdate(family)
    }
    return redirect("search", "info.save.success")
  }

  def profile(): View = {
    val role = entityDao.get(classOf[Role], intId("role"))
    val helper = new ProfileHelper(entityDao, profileService)
    helper.populateInfo(List(role))
    put("role", role)
    return forward()
  }

  def editProfile(): View = {
    val role = entityDao.get(classOf[Role], intId("role"))
    val me = entityDao.findBy(classOf[User], "code", List(Securities.user)).head
    val helper = new ProfileHelper(entityDao, profileService)
    helper.fillEditInfo(role, true, null)
    put("role", role)
    forward()
  }

  def removeProfile(): View = {
    val role = entityDao.get(classOf[Role], intId("role"))
    role.properties.clear()
    entityDao.saveOrUpdate(role)
    redirect("profile", "info.save.success")
  }

  def saveProfile(): View = {
    val me = entityDao.findBy(classOf[User], "code", List(Securities.user)).head
    val helper = new ProfileHelper(entityDao, profileService)
    helper.dataResolver = dataResolver
    val role = entityDao.get(classOf[Role], intId("role"))
    val app = entityDao.get(classOf[App], intId("app"))
    helper.populateSaveInfo(role, userService.isRoot(me, app.name), app.domain)
    entityDao.saveOrUpdate(role)
    redirect("profile", "info.save.success")
  }

  /**
   * 删除一个或多个角色
   */
  override def remove(): View = {
    val me = entityDao.findBy(classOf[User], "code", List(Securities.user)).head
    roleService.remove(me, entityDao.find(classOf[Role], intIds("role")))
    redirect("search", "info.remove.success")
  }

}
