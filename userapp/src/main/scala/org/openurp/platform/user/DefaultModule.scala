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
package org.openurp.platform.user

import org.beangle.cdi.bind.BindModule
import org.openurp.platform.user.action.{ IndexAction, MessageAction, NotificationAction, TodoAction }
import org.openurp.platform.user.action.NoticeAction
import org.openurp.platform.user.action.DocAction

class DefaultModule extends BindModule {

  protected override def binding() {
    bind(classOf[IndexAction], classOf[TodoAction], classOf[MessageAction], classOf[NotificationAction])

    bind(classOf[DocAction], classOf[NoticeAction])
  }
}
