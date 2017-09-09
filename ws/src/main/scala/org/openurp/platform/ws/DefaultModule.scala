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
package org.openurp.platform.ws

import org.beangle.cache.concurrent.ConcurrentMapCacheManager
import org.beangle.cdi.bind.BindModule
import org.openurp.platform.oauth.service.impl.MemTokenRepository
import org.openurp.platform.ws.config.DatasourceWS
import org.openurp.platform.ws.oauth.TokenWS
import org.openurp.platform.ws.user.AppWS
import org.openurp.platform.ws.user.RootWS
import org.openurp.platform.ws.user.{ AccountWS, DimensionWS, ProfileWS }
import org.openurp.platform.ws.security.{ func, data }
import org.openurp.platform.ws.config.OrgWS

class DefaultModule extends BindModule {

  protected override def binding() {
    bind(classOf[DatasourceWS], classOf[OrgWS])
    bind(classOf[TokenWS])

    bind(classOf[func.MenuWS])
    bind(classOf[func.ResourceWS], classOf[func.PermissionWS])
    bind(classOf[data.PermissionWS], classOf[data.ResourceWS])

    bind(classOf[AccountWS], classOf[AppWS], classOf[DimensionWS])
    bind(classOf[RootWS], classOf[ProfileWS])

    bind(classOf[MemTokenRepository], classOf[ConcurrentMapCacheManager])
  }
}
