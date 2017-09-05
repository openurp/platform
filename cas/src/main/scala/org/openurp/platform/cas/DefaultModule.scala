package org.openurp.platform.cas

import java.io.FileInputStream

import org.beangle.cache.redis.JedisPoolFactory
import org.beangle.cdi.PropertySource
import org.beangle.cdi.bind.BindModule
import org.beangle.commons.collection.Collections
import org.beangle.ids.cas.id.impl.DefaultServiceTicketIdGenerator
import org.beangle.ids.cas.ticket.{ DefaultTicketCacheService, DefaultTicketRegistry }
import org.beangle.security.authz.PublicAuthorizer
import org.beangle.security.web.{ UrlEntryPoint, WebSecurityManager }
import org.beangle.security.web.access.{ DefaultAccessDeniedHandler, SecurityInterceptor }
import org.openurp.app.UrpApp

/**
 * @author chaostone
 */
class DefaultModule extends BindModule with PropertySource {

  override def binding() {
    // entry point
    bind("security.EntryPoint.url", classOf[UrlEntryPoint]).constructor("/login").primary()
    //interceptor
    bind("security.AccessDeniedHandler.default", classOf[DefaultAccessDeniedHandler]).constructor($("security.access.errorPage", "/403.html"))
    bind("web.Interceptor.security", classOf[SecurityInterceptor])
    //authorizer and manager
    bind("security.SecurityManager.default", classOf[WebSecurityManager])
    bind("security.Authorizer.public", PublicAuthorizer)
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
      is.close()
    }
    datas.toMap
  }
}


