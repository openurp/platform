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
package org.openurp.platform.ws

import org.beangle.cache.caffeine.CaffeineCacheManager
import org.beangle.cdi.bind.BindModule
import org.openurp.platform.oauth.service.impl.MemTokenRepository
import org.openurp.platform.ws.bulletin.NoticeWS
import org.openurp.platform.ws.config.{DatasourceWS, OrgWS}
import org.openurp.platform.ws.oauth.TokenWS
import org.openurp.platform.ws.security.{data, func}
import org.openurp.platform.ws.user._

class DefaultModule extends BindModule {

  protected override def binding(): Unit = {
    bind(classOf[DatasourceWS], classOf[OrgWS])
    bind(classOf[TokenWS])

    bind(classOf[NoticeWS])

    bind(classOf[func.MenuWS])
    bind(classOf[func.ResourceWS], classOf[func.PermissionWS])
    bind(classOf[data.PermissionWS], classOf[data.ResourceWS])

    bind(classOf[AccountWS], classOf[AppWS], classOf[DimensionWS], classOf[AvatarWS])
    bind(classOf[RootWS], classOf[ProfileWS], classOf[CredentialWS])

    bind(classOf[MemTokenRepository])
    bind("cache.Caffeine", classOf[CaffeineCacheManager]).constructor(true)
  }
}
