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

import org.beangle.commons.collection.Order
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.{Condition, OqlBuilder}
import org.beangle.security.Securities
import org.beangle.security.authc.DBCredentialStore
import org.beangle.security.codec.DefaultPasswordEncoder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.user.model.{User, UserCategory}
import org.openurp.platform.user.service.UserService

/**
 * 用户管理响应处理类
 * @author chaostone 2005-9-29
 */
class AccountAction extends RestfulAction[User] {

  var userService: UserService = _

  var credentialStore: DBCredentialStore = _

  override def indexSetting(): Unit = {
    put("categories", entityDao.getAll(classOf[UserCategory]))
  }

  protected override def getQueryBuilder: OqlBuilder[User] = {
    val userQuery = OqlBuilder.from(classOf[User], "user")
    // 查询角色
    val sb = new StringBuilder("exists(from user.members m where ")
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
    userQuery.tailOrder("user.id")
    userQuery.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    userQuery
  }

  /**
   * 保存用户信息
   */
  protected override def saveAndRedirect(user: User): View = {
    if (entityDao.duplicate(classOf[User], user.id, "code", user.code)) {
      addMessage("security.error.usernameNotAvaliable", user.code)
      return forward(to(this, "edit"))
    }
    // 检验用户合法性
    val errorMsg = checkUser(user)
    if (Strings.isNotEmpty(errorMsg)) {
      return forward(to(this, "edit"), errorMsg)
    }
    if (!user.persisted) {
      userService.create(loginUser, user)
    } else {
      entityDao.saveOrUpdate(user)
    }
    processPassword(user)
    redirect("search", "info.save.success")
  }

  protected override def editSetting(user: User): Unit = {
    put("categories", entityDao.getAll(classOf[UserCategory]))
  }

  private def loginUser: User = {
    entityDao.findBy(classOf[User], "code", List(Securities.user)).head
  }

  /**
   * 删除一个或多个用户
   */
  override def remove(): View = {
    val userIds = longIds("user")
    val creator = loginUser
    val toBeRemoved = userService.getUsers(userIds: _*)
    val sb = new StringBuilder()
    var removed: User = null
    var success = 0
    var expected = toBeRemoved.size
    try {
      for (one <- toBeRemoved) {
        removed = one.asInstanceOf[User]
        // 不能删除自己
        if (one.id != creator.id) {
          userService.remove(creator, removed)
          success += 1
        } else {
          addFlashError("security.info.cannotRemoveSelf")
          expected -= 1
        }
      }
    } catch {
      case _: Exception => sb.append(',').append(removed.getName())
    }
    if (sb.nonEmpty) {
      sb.deleteCharAt(0)
      addFlashMessage("security.info.userRemovePartial", success, sb)
    } else if (expected == success && success > 0) {
      addFlashMessage("info.remove.success")
    }
    redirect("search")
  }

  /**
   * 禁用或激活一个或多个用户
   */
  def activate(): View = {
    val userIds = longIds("user")
    val isActivate = get("isActivate", "true")
    var successCnt: Int = 0
    val manager = loginUser
    var msg = "security.info.freeze.success"
    if (Strings.isNotEmpty(isActivate) && "false".equals(isActivate)) {
      successCnt = userService.updateState(manager, userIds, active = false)
    } else {
      msg = "security.info.activate.success"
      successCnt = userService.updateState(manager, userIds, active = true)
    }
    addFlashMessage(msg, successCnt)
    redirect("search")
  }

  protected def checkUser(user: User): String = {
    if (!user.persisted && entityDao.exists(entityName, "code", user.code)) "error.model.existed"
    else ""
  }

  protected def processPassword(user: User): Unit = {
    var password = get("password").orNull
    if (Strings.isBlank(password) && !user.persisted) {
      password = user.code
    }
    if (Strings.isNotBlank(password)) {
      credentialStore.updatePassword(user.code, DefaultPasswordEncoder.generate(password, null, "sha"))
    }
  }

  protected override def simpleEntityName: String = {
    "user"
  }
}
