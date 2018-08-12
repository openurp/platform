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
package org.openurp.platform.web.action

import org.beangle.security.Securities
import org.beangle.security.realm.cas.{ Cas, CasConfig }
import org.beangle.webmvc.api.action.{ ActionSupport, ServletSupport }
import org.beangle.webmvc.api.view.View
import org.openurp.app.{ Urp, UrpApp }
import org.openurp.app.security.RemoteService
import org.beangle.commons.codec.digest.Digests

class IndexAction extends ActionSupport with ServletSupport {
  def index(): View = {
    put("appsJson", RemoteService.getAppsJson())
    put("menusJson", RemoteService.getMenusJson())
    put("appName", UrpApp.name)
    put("URP", Urp)
    put("org", RemoteService.getOrg)
    val user = Securities.session.get.principal
    put("user", user)
    put("avatar_url", Urp.platformBase + "/user/avatars/" + Digests.md5Hex(user.getName))
    forward()
  }

  def logout(): View = {
    redirect(to("/cas/logout"), null)
  }
}
