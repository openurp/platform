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
package org.openurp.platform.cas.service

import org.beangle.commons.lang.Strings
import org.beangle.ids.cas.id.impl.DefaultIdGenerator
import org.beangle.security.web.session.CookieSessionIdPolicy
import org.openurp.app.Urp

import javax.servlet.http.HttpServletRequest

/**
 * @author chaostone
 */
class DefaultUrpSessionIdPolicy extends CookieSessionIdPolicy("URP_SID") {
  private val sessionIdGenerator = new DefaultIdGenerator("URP-", 35)

  this.domain = makeDomain()

  private def makeDomain(): String = {
    var base = Strings.substringAfter(Urp.base, "//")
    //remove port
    if (base.contains(":")) {
      base = Strings.substringBefore(base, ":")
    }
    base
  }

  protected override def generateId(request: HttpServletRequest): String = {
    sessionIdGenerator.nextid()
  }

}
