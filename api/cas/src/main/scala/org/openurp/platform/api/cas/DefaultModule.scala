package org.openurp.platform.api.cas

import java.io.{ File, FileInputStream }

import org.beangle.cdi.PropertySource
import org.beangle.cdi.bind.BindModule
import org.beangle.commons.collection.Collections
import org.beangle.commons.lang.Strings
import org.beangle.data.jdbc.ds.DataSourceFactory
import org.beangle.security.authc.{ DefaultAccountRealm, RealmAuthenticator }
import org.beangle.security.realm.cas.{ CasConfig, CasEntryPoint, CasPreauthFilter, DefaultTicketValidator }
import org.beangle.security.session.jdbc.DBSessionRegistry
import org.beangle.security.web.WebSecurityManager
import org.beangle.security.web.access.{ AuthorizationFilter, DefaultAccessDeniedHandler, SecurityInterceptor }
import org.openurp.platform.api.Urp
import org.openurp.platform.api.security.{ DefaultUrpSessionIdPolicy, RemoteAccountStore, RemoteAuthorizer }
import org.beangle.cache.caffeine.CaffeineCacheManager
import org.beangle.security.session.http.HttpSessionRepo

class DefaultModule extends BindModule with PropertySource {

  override def binding() {
    // entry point
    bind("security.EntryPoint.cas", classOf[CasEntryPoint]).primary()

    //interceptor and filters
    bind("security.AccessDeniedHandler.default", classOf[DefaultAccessDeniedHandler])
      .constructor($("security.access.errorPage", "/403.html"))
    bind("security.Filter.authorization", classOf[AuthorizationFilter])
    bind("security.Filter.cas", classOf[CasPreauthFilter])
    bind("web.Interceptor.security", classOf[SecurityInterceptor]).constructor(
      List(ref("security.Filter.cas"), ref("security.Filter.authorization")), ?, ?, ?)

    bind("security.Realm.default", classOf[DefaultAccountRealm]).constructor(bean(classOf[RemoteAccountStore]))
    bind("security.Authenticator", classOf[RealmAuthenticator]).constructor(List(ref("security.Realm.default")))

    bind("cache.Caffeine", classOf[CaffeineCacheManager])

    bind("security.SessionRepo.http", classOf[CasHttpSessionRepo])
      .constructor(ref("cache.Caffeine"), ref("casConfig"))

    bind("security.SessionIdPolicy.cookie", classOf[DefaultUrpSessionIdPolicy])
      .property("path", "/").property("domain", Strings.substringAfter(Urp.base, "//"))

    //cas
    bind("casConfig", classOf[CasConfig]).constructor($("openurp.platform.cas.server"))
    bind("security.TicketValidator.default", classOf[DefaultTicketValidator])

    //authorizer and manager
    bind("security.SecurityManager.default", classOf[WebSecurityManager])
    bind("security.Authorizer.remote", classOf[RemoteAuthorizer]).constructor(ref("cache.Caffeine"))
  }

  override def properties: collection.Map[String, String] = {
    val datas = Collections.newMap[String, String]
    var casUrl = Urp.properties.get("openurp.platform.cas.server").getOrElse(Urp.platformBase + "/cas")
    if (!casUrl.startsWith("http")) casUrl = "http://" + casUrl
    datas += ("openurp.platform.cas.server" -> casUrl)
    datas.toMap
  }
}