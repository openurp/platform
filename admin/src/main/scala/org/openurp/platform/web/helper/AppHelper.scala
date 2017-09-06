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
package org.openurp.platform.web.helper
import org.beangle.webmvc.api.context.Params
import org.beangle.webmvc.api.context.ActionContext
import org.beangle.data.dao.EntityDao
import org.openurp.platform.config.model.App
import org.beangle.data.dao.OqlBuilder
import org.beangle.commons.lang.Numbers
import org.beangle.commons.web.util.CookieUtils

object AppHelper {

  def putApps(apps: Iterable[App], appParamName: String, entityDao: EntityDao): Unit = {
    Params.getInt(appParamName) match {
      case Some(id) =>
        ActionContext.current.attribute("current_app", findApp(apps, id))
      case None =>
        val c = ActionContext.current
        val appId = CookieUtils.getCookieValue(c.request, "urp_app_id")
        c.attribute("current_app", findApp(apps, Numbers.toInt(appId)))
    }
    ActionContext.current.attribute("apps", apps)
  }

  def remember(appParamName: String): Unit = {
    Params.getInt(appParamName) foreach { id =>
      val c = ActionContext.current
      CookieUtils.addCookie(c.request, c.response, "urp_app_id", id.toString, -1)
    }
  }

  private def findApp(apps: Iterable[App], appId: Int): App = {
    apps.find(a => a.id == appId) match {
      case Some(app) => app
      case None => apps.head
    }
  }

}
