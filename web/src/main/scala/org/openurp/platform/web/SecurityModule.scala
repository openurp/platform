/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2005, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.platform.web

import org.beangle.cdi.bind.BindModule
import org.openurp.platform.web.action.security.{ DashboardAction, DataPermissionAction, DataResourceAction, FuncResourceAction, MenuAction, PermissionAction }
import org.openurp.platform.web.action.user.{ AccountAction, DimensionAction, RoleAction, UserAction, ProfileAction }
import org.beangle.cache.concurrent.ConcurrentMapCacheManager
import org.openurp.platform.web.helper.UserDashboardHelper
import org.openurp.platform.web.action.IndexAction

class SecurityModule extends BindModule {

  protected override def binding() {
    bind(classOf[AccountAction], classOf[DashboardAction])
    bind(classOf[DimensionAction], classOf[PermissionAction], classOf[UserAction], classOf[RoleAction], classOf[ProfileAction])
    bind(classOf[FuncResourceAction], classOf[MenuAction])
    bind(classOf[DataPermissionAction])
    bind(classOf[DataResourceAction])

    bind(classOf[IndexAction])

    bind("userDashboardHelper", classOf[UserDashboardHelper])
  }
}
