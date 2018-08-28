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
package org.openurp.platform.action

import org.beangle.cdi.bind.BindModule
import org.openurp.platform.action.admin.IndexAction
import org.openurp.platform.action.admin.bulletin.{ DocAction, NewsAction, NoticeAction }
import org.openurp.platform.action.admin.security.{ DashboardAction, DataPermissionAction, DataResourceAction, FuncResourceAction, MenuAction, PermissionAction }
import org.openurp.platform.action.admin.user.{ AccountAction, AvatarAction, DimensionAction, MessageAction, NotificationAction, ProfileAction, RoleAction, TodoAction, UserAction }
import org.openurp.platform.action.helper.UserDashboardHelper

class SecurityModule extends BindModule {

  protected override def binding() {
    bind(classOf[AccountAction], classOf[DashboardAction], classOf[AvatarAction])
    bind(classOf[DimensionAction], classOf[PermissionAction], classOf[UserAction],
      classOf[RoleAction], classOf[ProfileAction])

    bind(classOf[TodoAction], classOf[MessageAction], classOf[NotificationAction])

    bind(classOf[DocAction], classOf[NoticeAction], classOf[NewsAction])

    bind(classOf[FuncResourceAction], classOf[MenuAction])
    bind(classOf[DataPermissionAction])
    bind(classOf[DataResourceAction])

    bind(classOf[IndexAction])

    bind("userDashboardHelper", classOf[UserDashboardHelper])
  }
}
