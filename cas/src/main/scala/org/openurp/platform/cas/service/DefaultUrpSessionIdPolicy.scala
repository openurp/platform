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
package org.openurp.platform.cas.service

import javax.servlet.http.HttpServletRequest
import org.beangle.ids.cas.id.impl.DefaultIdGenerator
import org.beangle.security.web.session.CookieSessionIdPolicy
import org.openurp.app.Urp

/**
 * @author chaostone
 */
class DefaultUrpSessionIdPolicy extends CookieSessionIdPolicy("URP_SID") {
  private val sessionIdGenerator = new DefaultIdGenerator("URP-", 35)

  override def init(): Unit = {
    if (null == this.domain) {
      this.base = Urp.base
    }
  }

  protected override def generateId(request: HttpServletRequest): String = {
    sessionIdGenerator.nextid()
  }

}
