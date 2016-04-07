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
package org.openurp.platform.web.helper

import org.beangle.data.dao.EntityDao
import org.beangle.security.session.SessionRegistry
import org.beangle.webmvc.api.context.{ ActionContext, Params }
import org.openurp.platform.security.service.{ FuncPermissionService, MenuService, ProfileService }
import org.openurp.platform.security.model.MenuProfile
import org.openurp.platform.security.model.Menu
import org.openurp.platform.user.model.Role
import org.openurp.platform.user.model.User
import org.openurp.platform.user.model.UserProfile

/**
 * @author chaostone
 * @version $Id: DashboardHelper.java Nov 3, 2010 5:19:42 PM chaostone $
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
    populateMenus(user)
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

  private def populateMenus(user: User) {
    val menuProfiles = menuService.getProfiles(user)
    ActionContext.current.attribute("menuProfiles", menuProfiles)
    var menuProfileId = Params.getInt("menuProfileId")
    if (None == menuProfileId && !menuProfiles.isEmpty) {
      menuProfileId = Some(menuProfiles(0).id)
    }

    if (None != menuProfileId) {
      val menuProfile = entityDao.get(classOf[MenuProfile], menuProfileId.get)
      val menus = menuService.getMenus(menuProfile, user)
      val resources = permissionService.getResources(user).toSet
      val roleMenusMap = new collection.mutable.HashMap[Role, Seq[Menu]]

      for (m <- user.roles) {
        if (m.member) roleMenusMap.put(m.role, menuService.getMenus(menuProfile, m.role))
      }
      ActionContext.current.attribute("menus", menus)
      ActionContext.current.attribute("roleMenusMap", roleMenusMap)
      ActionContext.current.attribute("resources", resources)
    }
  }

}
