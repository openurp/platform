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
package org.openurp.platform.admin.action.user

import java.time.Instant

import org.beangle.commons.collection.{ Collections, Order }
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.{ Condition, Operation, OqlBuilder }
import org.beangle.security.Securities
import org.beangle.webmvc.api.context.Params
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.app.UrpApp
import org.openurp.platform.user.model.{ MemberShip, Role, RoleMember, User }
import org.openurp.platform.user.service.UserService
import org.openurp.platform.admin.helper.UserDashboardHelper

/**
 * 用户管理响应处理类
 *
 * @author chaostone 2005-9-29
 */
class UserAction extends RestfulAction[User] {

  var userService: UserService = _
  var userDashboardHelper: UserDashboardHelper = _

  override def info(id: String): View = {
    val userId = Params.converter.convert(id, classOf[Long])
    var managed: User = null
    if (None != userId) {
      managed = entityDao.get(classOf[User], userId.get)
    } else {
      get("user.name") foreach { name =>
        managed = userService.get(name).orNull.asInstanceOf[User]
      }
    }
    val me = loginUser
    if (null != managed) {
      if (me.equals(managed) || userService.isManagedBy(me, managed)) {
        userDashboardHelper.buildDashboard(managed)
        return forward()
      } else {
        throw new RuntimeException("not belong to u")
      }
    } else {
      userDashboardHelper.buildDashboard(me)
    }
    return forward()
  }

  private def loginUser: User = {
    entityDao.findBy(classOf[User], "code", List(Securities.user)).head
  }

  protected override def getQueryBuilder(): OqlBuilder[User] = {
    val manager = loginUser
    val userQuery = OqlBuilder.from(classOf[User], "user")
    // 查询角色
    val sb = new StringBuilder("exists(from user.roles m where ")
    val params = new collection.mutable.ListBuffer[Object]
    var queryRole = false
    val roleName = get("roleName", "")
    if (Strings.isNotEmpty(roleName)) {
      if (queryRole) sb.append(" and ")
      sb.append("m.role.name like :roleName ")
      params += ("%" + roleName + "%")
      queryRole = true
    }
    if (queryRole) {
      sb.append(')')
      val roleCondition = new Condition(sb.toString())
      roleCondition.params(params)
      userQuery.where(roleCondition)
    }
    populateConditions(userQuery)
    userQuery.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    return userQuery
  }

  /**
   * 保存用户信息
   */
  protected override def saveAndRedirect(entity: User): View = {
    val user = entity.asInstanceOf[User]
    val userMembers = user.roles
    val memberMap = new collection.mutable.HashMap[Role, RoleMember]
    for (gm <- userMembers) {
      memberMap.put(gm.role, gm.asInstanceOf[RoleMember])
    }
    val newMembers = Collections.newBuffer[RoleMember]
    val removedMembers = Collections.newBuffer[RoleMember]
    val manager = loginUser
    val platformAdmin = userService.isRoot(manager, UrpApp.name)
    val members =
      if (platformAdmin) {
        entityDao.search(OqlBuilder.from(classOf[Role], "r")).map(r => new RoleMember(manager, r, MemberShip.Granter))
      } else {
        userService.getRoles(manager, MemberShip.Granter)
      }
    for (member <- members) {
      var myMember = memberMap.getOrElse(member.role, null)
      val isMember = getBoolean("member" + member.role.id, false)
      val isGranter = getBoolean("granter" + member.role.id, false)
      val isManager = getBoolean("manager" + member.role.id, false)
      if (!isMember && !isGranter && !isManager) {
        if (null != myMember) {
          user.roles -= myMember
          removedMembers += myMember
        }
      } else {
        if (null == myMember) myMember = new RoleMember(user, member.role)
        myMember.updatedAt = Instant.now
        myMember.member = isMember
        myMember.granter = isGranter
        myMember.manager = isManager
        newMembers += myMember
      }
    }
    val ob = new Operation.Builder()
    for (m <- newMembers) ob.saveOrUpdate(m)
    for (m <- removedMembers) ob.remove(m)
    entityDao.execute(ob)
    entityDao.refresh(user)
    return redirect("search", "info.save.success")
  }

  protected override def editSetting(user: User) {
    val manager = loginUser
    val roles = new collection.mutable.HashSet[Role]
    val mngMemberMap = new collection.mutable.HashMap[Role, RoleMember]
    val platformAdmin = userService.isRoot(manager, UrpApp.name)
    if (platformAdmin) {
      roles ++= entityDao.search(OqlBuilder.from(classOf[Role], "r").orderBy("r.indexno"))
      for (role <- roles)
        mngMemberMap.put(role, new RoleMember(manager, role, MemberShip.Granter))
    } else {
      val members = userService.getRoles(manager, MemberShip.Granter)
      for (gm <- members) {
        roles.add(gm.role)
        mngMemberMap.put(gm.role, gm)
      }
    }
    put("roles", roles)

    val memberMap = new collection.mutable.HashMap[Role, RoleMember]
    for (gm <- user.roles) {
      memberMap.put(gm.role, gm)
    }
    put("memberMap", memberMap)
    put("mngMemberMap", mngMemberMap)
    put("isme", manager.id == user.id)
  }
}
