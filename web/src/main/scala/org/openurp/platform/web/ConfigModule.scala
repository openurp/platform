/*
 * Beangle, Agile Development Scaffold and Toolkit
 *
 * Copyright (c) 2005-2017, Beangle Software.
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
package org.openurp.platform.web

import org.beangle.cdi.bind.BindModule
import org.openurp.platform.web.action.config.{ AppAction, DbAction }
import org.openurp.platform.web.action.config.AppAction
import org.openurp.platform.web.action.config.DbAction

class ConfigModule extends BindModule {

  protected override def binding() {
    bind(classOf[AppAction])
    bind(classOf[DbAction])
  }
}