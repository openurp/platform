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
package org.openurp.kernel.security.action

import java.util.Date
import org.beangle.webmvc.entity.action.RestfulAction
import org.beangle.commons.lang.Strings
import org.beangle.data.jpa.dao.OqlBuilder
import org.beangle.data.model.dao.Condition
import org.beangle.commons.collection.Order
import org.beangle.webmvc.api.view.View
import org.beangle.commons.codec.digest.Digests
import org.beangle.webmvc.api.action.ActionSupport._
import org.beangle.data.model.dao.Operation
import org.beangle.webmvc.api.context.Params
import org.beangle.webmvc.api.annotation.action
import org.openurp.kernel.security.model.UrpUserBean
import org.openurp.kernel.security.model.Member
import org.openurp.kernel.security.service.UserManager
/**
 * 用户管理响应处理类
 *
 * @author chaostone 2005-9-29
 */
class AccountAction extends RestfulAction[UrpUserBean] {

  var userManager: UserManager = _

  //FIXME
  def getUserId(): java.lang.Long = {
    1L
  }
  //FXIME
  def isAdmin(): Boolean = {
    true
  }

  protected override def getQueryBuilder(): OqlBuilder[UrpUserBean] = {
    val manager = entityDao.get(classOf[UrpUserBean], getUserId())
    val userQuery = OqlBuilder.from(classOf[UrpUserBean], "user")
    // 查询角色
    val sb = new StringBuilder("exists(from user.members m where ")
    val params = new collection.mutable.ListBuffer[Object]
    var queryRole = false
    if (!isAdmin()) {
      val members = userManager.getMembers(manager, Member.Ship.IsManager)
      val mngRoles = members.map(m => m.role)
      if (mngRoles.isEmpty) {
        sb.append("1=0")
      } else {
        sb.append("m.role in(:roles) and m.member=true")
        params += mngRoles
      }
      queryRole = true
      userQuery.where("user.id != :meId", getUserId())
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
    userQuery.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit())
    return userQuery
  }

  //
  //  protected PropertyExtractor getPropertyExtractor() {
  //    return new UserPropertyExtractor(getTextResource())
  //  }

  /**
   * 保存用户信息
   */
  protected def saveAndForward(user: UrpUserBean): View = {
    if (entityDao.duplicate(classOf[UrpUserBean], user.id, "name", user.code)) {
      addMessage("security.error.usernameNotAvaliable", user.code)
      return forward(to(this, "edit"))
    }
    // 检验用户合法性
    var errorMsg = checkUser(user)
    if (Strings.isNotEmpty(errorMsg)) { return forward(to(this, "edit"), errorMsg); }
    processPassword(user)
    if (!user.persisted) {
      val creator = userManager.get(getUserId()).asInstanceOf[UrpUserBean]
      userManager.create(creator, user)
    } else {
      entityDao.saveOrUpdate(user)
    }
    return redirect("search", "info.save.success")
  }

  protected override def editSetting(user: UrpUserBean) {
    //    val manager = entityDao.get(classOf[User], getUserId())
    //    val roles = new collection.mutable.HashSet[Role]
    //    val curMemberMap = new collection.mutable.HashMap[Role, Member]
    //    val members = userManager.getMembers(manager, Member.Ship.IsGranter)
    //    for (gm <- members) {
    //      roles.add(gm.role)
    //      curMemberMap.put(gm.role, gm)
    //    }
    //    put("roles", roles)
    //
    //    val userMembers = user.members
    //    val memberMap = new collection.mutable.HashMap[Role, Member]
    //    for (gm <- userMembers) {
    //      memberMap.put(gm.role, gm)
    //    }
    //    put("memberMap", memberMap)
    //    put("curMemberMap", curMemberMap)
    //    put("isadmin", userManager.isRoot(user))
    //    put("isme", getUserId() == user.id)
    //    put("settings", new Settings(getConfig()))
  }

  /**
   * 删除一个或多个用户
   */
  override def remove(): View = {
    val userIds = getLongIds("user")
    val creator = userManager.get(getUserId()).asInstanceOf[UrpUserBean]
    val toBeRemoved = userManager.getUsers(userIds: _*)
    val sb = new StringBuilder()
    var removed: UrpUserBean = null
    var success = 0
    var expected = toBeRemoved.size
    try {
      for (one <- toBeRemoved) {
        removed = one.asInstanceOf[UrpUserBean]
        // 不能删除自己
        if (!one.id.equals(getUserId())) {
          userManager.remove(creator, removed)
          success += 1
        } else {
          addFlashError("security.info.cannotRemoveSelf")
          expected -= 1
        }
      }
    } catch {
      case e: Exception => sb.append(',').append(removed.getName())
    }
    if (sb.length() > 0) {
      sb.deleteCharAt(0)
      addFlashMessage("security.info.userRemovePartial", success, sb)
    } else if (expected == success && success > 0) {
      addFlashMessage("info.remove.success")
    }
    return redirect("search")
  }

  /**
   * 禁用或激活一个或多个用户
   */
  def activate(): View = {
    val userIds = getLongIds("user")
    val isActivate = get("isActivate", "true")
    var successCnt: Int = 0
    val manager = userManager.get(getUserId()).asInstanceOf[UrpUserBean]
    var msg = "security.info.freeze.success"
    if (Strings.isNotEmpty(isActivate) && "false".equals(isActivate)) {
      successCnt = userManager.updateState(manager, userIds, false)
    } else {
      msg = "security.info.activate.success"
      successCnt = userManager.updateState(manager, userIds, true)
    }
    addFlashMessage(msg, successCnt)
    return redirect("search")
  }

  protected def checkUser(user: UrpUserBean): String = {
    if (!user.persisted && entityDao.exists(entityName, "name", user.code)) "error.model.existed";
    else ""
  }

  protected def processPassword(user: UrpUserBean) {
    var password = get("password").orNull
    if (Strings.isNotBlank(password)) {
      user.password = Digests.md5Hex(password)
    } else if (!user.persisted) {
      user.password = Digests.md5Hex("123456")
    }
  }

  protected override def shortName: String = {
    "user"
  }
}
