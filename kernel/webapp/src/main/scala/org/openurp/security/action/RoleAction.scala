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

import java.sql.Date

import org.beangle.data.jpa.dao.OqlBuilder
import org.beangle.data.model.util.Hierarchicals
import org.beangle.security.blueprint.{ Role, User }
import org.beangle.security.blueprint.service.{ DataResolver, ProfileService, UserService }
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.app.App
import org.openurp.security.helper.ProfileHelper
import org.openurp.security.model.UrpRoleBean
import org.openurp.security.service.RoleManager
/**
 * 角色信息维护响应类
 *
 * @author chaostone 2005-9-29
 */
class RoleAction(val roleManager: RoleManager, val userService: UserService) extends RestfulAction[Role] {

  var dataResolver: DataResolver = _

  var profileService: ProfileService = _

  def getUserId(): java.lang.Long = {
    //SecurityContext.session.principal.asInstanceOf[Account].id.asInstanceOf[java.lang.Long]
    1L
  }
  def isAdmin(): Boolean = {
    true
  }
  protected override def indexSetting(): Unit = {
    put("apps", entityDao.getAll(classOf[App]))
  }
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
    if (!isAdmin()) {
      query.join("role.members", "gm")
      query.where("gm.user.id=:me and gm.manager=true", getUserId())
    }
    val parents = new collection.mutable.ListBuffer[Role]
    parents ++= entityDao.search(query)
    parents --= Hierarchicals.getFamily(role)
    put("parents", parents)

    put("apps", entityDao.getAll(classOf[App]))
    forward()
  }

  protected override def getQueryBuilder(): OqlBuilder[Role] = {
    val entityQuery = OqlBuilder.from(classOf[Role], "role")
    if (!isAdmin()) {
      entityQuery.join("role.members", "gm")
      entityQuery.where("gm.user.id=:me and gm.manager=true", getUserId())
    }
    populateConditions(entityQuery)
    val orderBy = get("orderBy", "role.indexno")
    entityQuery.limit(getPageLimit()).orderBy(orderBy)
  }
  //
  //  protected PropertyExtractor getPropertyExtractor() {
  //    return new RolePropertyExtractor(getTextResource())
  //  }

  protected override def saveAndRedirect(entity: Role): View = {
    val role = entity.asInstanceOf[UrpRoleBean]
    if (entity.persisted) {
      if (!roleManager.isManagedBy(entityDao.get(classOf[User], getUserId()), role)) {
        return redirect("search", "不能修改该组,你没有" + role.parent.name + "的管理权限");
      }
    }
    if (entityDao.duplicate(classOf[Role], role.id, "name", role.getName())) return redirect("edit",
      "error.notUnique")
    if (!role.persisted) {
      val creator = userService.get(getUserId())
      role.indexno = "tmp"
      role.creator = creator
      roleManager.create(creator, role)
    } else {
      role.updatedAt = new Date(System.currentTimeMillis)
      entityDao.saveOrUpdate(role)
    }
    var parent: Role = null
    val indexno = getInt("indexno", 1)
    get("parent.id", classOf[Integer]) match {
      case Some(parentId) => parent = entityDao.get(classOf[Role], parentId)
      case None =>
    }
    roleManager.move(role, parent, indexno)
    if (!role.enabled) {
      val family = Hierarchicals.getFamily(role.asInstanceOf[Role])
      for (one <- family) one.asInstanceOf[UrpRoleBean].enabled = false
      entityDao.saveOrUpdate(family)
    }
    return redirect("search", "info.save.success")
  }

  def profile(): String = {
    val role = entityDao.get(classOf[Role], getIntId("role"))
    val helper = new ProfileHelper(entityDao, profileService)
    helper.populateInfo(List(role))
    put("role", role)
    return forward()
  }

  def editProfile(): String = {
    val role = entityDao.get(classOf[Role], getIntId("role"))
    val helper = new ProfileHelper(entityDao, profileService)
    helper.fillEditInfo(role, isAdmin())
    put("role", role)
    forward()
  }

  def removeProfile(): View = {
    val role = entityDao.get(classOf[Role], getIntId("role"))
    role.properties.clear()
    entityDao.saveOrUpdate(role)
    redirect("profile", "info.save.success")
  }

  def saveProfile(): View = {
    val helper = new ProfileHelper(entityDao, profileService)
    helper.dataResolver = dataResolver
    val role = entityDao.get(classOf[Role], getIntId("role"))
    helper.populateSaveInfo(role, isAdmin())
    entityDao.saveOrUpdate(role)
    redirect("profile", "info.save.success")
  }

  /**
   * 删除一个或多个角色
   */
  override def remove(): View = {
    val curUser = userService.get(getUserId())
    roleManager.remove(curUser, entityDao.find(classOf[UrpRoleBean], getIntIds("role")))
    redirect("search", "info.remove.success")
  }

}
