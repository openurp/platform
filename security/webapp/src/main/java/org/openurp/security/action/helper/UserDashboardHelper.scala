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
package org.openurp.platform.security.action.helper

import java.util.List
import java.util.Map
import java.util.Set
import org.beangle.data.model.dao.EntityDao
import org.openurp.platform.security.FuncResource
import org.openurp.platform.security.MenuProfile
import org.openurp.platform.security.Role
import org.openurp.platform.security.Menu
import org.beangle.security.context.ContextHolder
import org.beangle.webmvc.api.context.Params
import org.openurp.platform.security.service.FuncPermissionService
import org.openurp.platform.security.service.ProfileService
import org.beangle.security.session.SessionRegistry
import org.beangle.commons.collection.page.Page
import org.openurp.platform.security.User
import org.beangle.webmvc.api.context.ContextHolder

/**
 * @author chaostone
 * @version $Id: DashboardHelper.java Nov 3, 2010 5:19:42 PM chaostone $
 */
class UserDashboardHelper {

  var entityDao:EntityDao=_ 

  var sessionRegistry:SessionRegistry =_

  var permissionService :FuncPermissionService =_

  var  menuService:MenuService =_

  var  profileService:ProfileService=_

  var sessioninfoLogService :SessioninfoLogService =_

   def buildDashboard(user:User ):Unit= {
    ContextHolder.context.attribute("user", user)
    populateMenus(user)
    populateSessioninfoLogs(user)
    populateOnlineActivities(user)
    new ProfileHelper(entityDao, profileService).populateInfo(user.getProfiles())
  }

  private def populateOnlineActivities(user:User) {
    ContextHolder.context.attribute("sessioninfos", sessionRegistry.getSessioninfos(user.code, true))
  }

  private def populateSessioninfoLogs(user:User) {
    List<SessioninfoLogBean> page = sessioninfoLogService.getLoggers(user.code, 5)
    if (page instanceof Page) page = ((Page<SessioninfoLogBean>) page).getItems()
    ContextHolder.context.attribute("sessioninfoLogs", page)
  }

  private def populateMenus(user:User) {
    val menuProfiles = menuService.getProfiles(user)
    ContextHolder.context.attribute("menuProfiles", menuProfiles)
    var menuProfileId = Params.getInt("menuProfileId")
    if (null == menuProfileId && !menuProfiles.isEmpty()) {
      menuProfileId = menuProfiles.get(0).getId()
    }
    if (null != menuProfileId) {
      val menuProfile =  entityDao.get(classOf[MenuProfile], menuProfileId)
      val menus = menuService.getMenus(menuProfile, user, user.getProfiles())
      val resources = Collections.newHashSet(permissionService.getResources(user))
      val roleMenusMap = Collections.newHashMap()

      for (role <-  user.roles) {
        roleMenusMap.put(role, menuService.getMenus(menuProfile, role, Boolean.TRUE))
      }
      ContextHolder.context.attribute("menus", menus)
      ContextHolder.context.attribute("roleMenusMap", roleMenusMap)
      ContextHolder.context.attribute("resources", resources)
    }
  }

}
