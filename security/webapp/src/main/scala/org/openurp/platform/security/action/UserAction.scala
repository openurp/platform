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

import java.util.Date
import org.beangle.commons.collection.Order
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.dao.{ Condition, Operation }
import org.beangle.webmvc.api.context.Params
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.security.helper.UserDashboardHelper
import org.openurp.platform.security.model.{ Member, MemberShip, Role, User }
import org.openurp.platform.security.service.UserManager
import org.openurp.platform.api.security.Securities
import org.openurp.platform.kernel.model.App
/**
 * 用户管理响应处理类
 *
 * @author chaostone 2005-9-29
 */
class UserAction extends RestfulAction[User] {

  var userManager: UserManager = _
  private var userDashboardHelper: UserDashboardHelper = _

  protected override def indexSetting(): Unit = {
    val apps = entityDao.getAll(classOf[App])
    put("apps", apps)
  }

  override def info(id: String): String = {
    val userId = Params.converter.convert(id, classOf[Long])
    var managed: User = null
    if (None != userId) {
      managed = entityDao.get(classOf[User], userId.get)
    } else {
      get("user.name") foreach { name =>
        managed = userManager.get(name).orNull.asInstanceOf[User]
      }
    }
    val me = loginUser
    if (null != managed) {
      if (me.equals(managed) || userManager.isManagedBy(me, managed)) {
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
    val sb = new StringBuilder("exists(from user.members m where ")
    val params = new collection.mutable.ListBuffer[Object]
    var queryRole = false
    val app = entityDao.get(classOf[App], intId("app"))
    if (!userManager.isRoot(manager, app.name)) {
      val members = userManager.getMembers(manager, MemberShip.Manager)
      val mngRoles = members.map(m => m.role)
      if (mngRoles.isEmpty) {
        sb.append("1=0")
      } else {
        sb.append("m.role in(:roles) and m.member=true")
        params += mngRoles
      }
      queryRole = true
      userQuery.where("user.id != :meId", manager.id)
    }
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
  protected def saveAndForward(entity: User): View = {
    val user = entity.asInstanceOf[User]
    val userMembers = user.members
    val app = entityDao.get(classOf[App], intId("app"))
    val memberMap = new collection.mutable.HashMap[Role, Member]
    for (gm <- userMembers) {
      memberMap.put(gm.role, gm.asInstanceOf[Member])
    }
    val newMembers = new collection.mutable.HashSet[Member]
    val removedMembers = new collection.mutable.HashSet[Member]
    val manager = loginUser
    val members = userManager.getMembers(manager, MemberShip.Granter)
    for (member <- members) {
      var myMember = memberMap(member.role)
      val isMember = getBoolean("member" + member.role.id, false)
      val isGranter = getBoolean("granter" + member.role.id, false)
      val isManager = getBoolean("manager" + member.role.id, false)
      if (!isMember && !isGranter && !isManager) {
        if (null != myMember) {
          user.members -= myMember
          removedMembers.add(myMember)
        }
      } else {
        if (null == myMember) myMember = new Member(user, member.role)
        myMember.updatedAt = new Date()
        myMember.member = isMember
        myMember.granter = isGranter
        myMember.manager = isManager
        newMembers.add(myMember)
      }
    }
    entityDao.execute(Operation.saveOrUpdate(newMembers).remove(removedMembers))
    return redirect("search", "info.save.success")
  }

  protected override def editSetting(user: User) {
    val app = entityDao.get(classOf[App], intId("app"))
    val manager = loginUser
    val isAdmin = userManager.isRoot(user, app.name)
    val roles = new collection.mutable.HashSet[Role]
    val managerMemberMap = new collection.mutable.HashMap[Role, Member]
    val members = userManager.getMembers(manager, MemberShip.Granter)
    for (gm <- members) {
      roles.add(gm.role)
      managerMemberMap.put(gm.role, gm)
    }
    put("roles", roles)

    val memberMap = new collection.mutable.HashMap[Role, Member]
    for (gm <- user.members) {
      memberMap.put(gm.role, gm)
    }
    put("memberMap", memberMap)
    put("curMemberMap", managerMemberMap)
    //FIXME
    put("isadmin", isAdmin)
    put("isme", manager.id == user.id)
    put("app", app)
  }

  protected override def shortName: String = {
    "user"
  }
}
