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
package org.openurp.platform.admin.helper

import org.beangle.data.dao.EntityDao
import org.beangle.security.session.SessionRegistry
import org.beangle.webmvc.api.context.{ ActionContext, Params }
import org.openurp.platform.security.service.{ FuncPermissionService, MenuService, ProfileService }
import org.openurp.platform.security.model.Menu
import org.openurp.platform.user.model.Role
import org.openurp.platform.user.model.User
import org.openurp.platform.user.model.UserProfile

/**
 * @author chaostone
 */
class UserDashboardHelper {

  var entityDao: EntityDao = _

  var sessionRegistry: SessionRegistry = _

  var permissionService: FuncPermissionService = _

  var menuService: MenuService = _

  var profileService: ProfileService = _

  //  var sessioninfoLogService :SessioninfoLogService =_

  def buildDashboard(user: User): Unit = {
    ActionContext.current.attribute("user", user)
    //populateMenus(user)
    //    populateSessioninfoLogs(user)
    //    populateOnlineActivities(user)
    val myProfiles = entityDao.findBy(classOf[UserProfile], "user", List(user))
    new ProfileHelper(entityDao, profileService).populateInfo(myProfiles)
  }
  //
  //  private def populateOnlineActivities(user: User) {
  //    ActionContext.current.attribute("sessioninfos", sessionRegistry.getSessioninfos(user.code, true))
  //  }

  //  private def populateSessioninfoLogs(user:User) {
  //    List<SessioninfoLogBean> page = sessioninfoLogService.getLoggers(user.code, 5)
  //    if (page instanceof Page) page = ((Page<SessioninfoLogBean>) page).getItems()
  //    ActionContext.current.attribute("sessioninfoLogs", page)
  //  }

}
