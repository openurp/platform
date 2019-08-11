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
package org.openurp.platform.cas

import org.beangle.cdi.bind.BindModule
import org.beangle.ids.cas.web.action.{ LoginAction, LogoutAction, ServiceValidateAction, SessionAction }
import org.beangle.ids.cas.web.helper.CaptchaHelper
import org.openurp.platform.cas.web.EditAction

class WebModule extends BindModule {
  override def binding(): Unit = {
    bind(classOf[LoginAction])
    bind(classOf[ServiceValidateAction])
    bind(classOf[LogoutAction])
    bind(classOf[SessionAction])
    bind(classOf[EditAction])
    bind(classOf[CaptchaHelper])
  }
}
