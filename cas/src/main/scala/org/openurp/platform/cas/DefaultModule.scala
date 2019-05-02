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

import java.io.FileInputStream

import org.beangle.cache.redis.JedisPoolFactory
import org.beangle.cdi.PropertySource
import org.beangle.cdi.bind.BindModule
import org.beangle.commons.collection.Collections
import org.beangle.commons.lang.Strings
import org.beangle.ids.cas.id.impl.DefaultServiceTicketIdGenerator
import org.beangle.ids.cas.ticket.{DefaultTicketCacheService, DefaultTicketRegistry}
import org.beangle.ids.cas.LoginConfig
import org.beangle.security.authz.PublicAuthorizer
import org.beangle.security.web.{UrlEntryPoint, WebSecurityManager}
import org.beangle.security.web.access.{DefaultAccessDeniedHandler, SecurityInterceptor}
import org.beangle.security.web.access.DefaultSecurityContextBuilder
import org.beangle.security.web.access.AuthorizationFilter
import org.openurp.app.{UrpApp, Urp}
import org.openurp.app.security.RemoteAuthorizer

/**
 * @author chaostone
 */
class DefaultModule extends BindModule with PropertySource {

  override def binding() {
    // entry point
    bind("security.EntryPoint.url", classOf[UrlEntryPoint]).constructor("/login").primary()
    //interceptor
    bind("security.AccessDeniedHandler.default", classOf[DefaultAccessDeniedHandler])
      .constructor($("security.access.errorPage", "/403.html"))
    bind("security.Filter.authorization", classOf[AuthorizationFilter])
    bind("web.Interceptor.security", classOf[SecurityInterceptor])
      .property(
        "filters", List(ref("security.Filter.authorization")))
    //authorizer and manager
    bind("security.SecurityManager.default", classOf[WebSecurityManager])
    bind(classOf[DefaultSecurityContextBuilder])
    bind("security.Authorizer.public", PublicAuthorizer)

    bind("casConfig", classOf[LoginConfig])
      .property("enableCaptcha", $("login.enableCaptcha"))
      .property("forceHttps", $("login.forceHttps"))
      .property("key", $("login.key"))
      .property("origin", $("login.origin"))
  }

  override def properties: collection.Map[String, String] = {
    val datas = Collections.newMap[String, String]
    UrpApp.getUrpAppFile foreach { file =>
      val is = new FileInputStream(file)
      val app = scala.xml.XML.load(is)
      (app \\ "ldap") foreach { e =>
        datas += ("ldap.url" -> (e \\ "url").text.trim)
        datas += ("ldap.user" -> (e \\ "user").text.trim)
        datas += ("ldap.password" -> (e \\ "password").text.trim)
        datas += ("ldap.base" -> (e \\ "base").text.trim)
      }
      (app \\ "redis") foreach { e =>
        datas += ("redis.host" -> (e \\ "host").text.trim)
        datas += ("redis.port" -> (e \\ "port").text.trim)
      }
      (app \\ "config" \\ "login") foreach { n =>
        val e = n.asInstanceOf[scala.xml.Elem]
        datas += ("login.enableCaptcha" -> getAttribute(e, "enableCaptcha", "false"))
        datas += ("login.forceHttps" -> getAttribute(e, "forceHttps", "false"))
        datas += ("login.key" -> getAttribute(e, "key", Urp.base))
        datas += ("login.origin" -> getAttribute(e, "origin", Urp.base))
      }
      if (!datas.contains("login.origin")) {
        datas += ("login.key" -> Urp.base)
        datas += ("login.origin" -> Urp.base)
      }
      is.close()
    }
    datas.toMap
  }
  private def getAttribute(e: scala.xml.Elem, name: String, defaultValue: String): String = {
    val v = (e \ ("@" + name)).text.trim
    if (Strings.isEmpty(v)) {
      defaultValue
    } else {
      v
    }
  }
}
