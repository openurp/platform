package org.openurp.platform.api.cas

import org.beangle.commons.inject.PropertySource
import org.beangle.commons.inject.bind.AbstractBindModule
import org.beangle.security.authc.{ DefaultAccountRealm, RealmAuthenticator }
import org.beangle.security.mgt.DefaultSecurityManager
import org.beangle.security.realm.cas.{ CasConfig, CasEntryPoint, CasPreauthFilter, DefaultTicketValidator }
import org.beangle.security.session.mem.MemSessionRegistry
import org.beangle.security.web.access.{ AuthorizationFilter, DefaultAccessDeniedHandler, SecurityInterceptor }
import org.beangle.security.web.session.DefaultSessionIdPolicy
import org.openurp.platform.api.Urp

class DefaultModule extends AbstractBindModule with PropertySource {

  override def binding() {
    // entry point
    bind("security.EntryPoint.cas", classOf[CasEntryPoint]).primary()

    //interceptor and filters
    bind("security.AccessDeniedHandler.default", classOf[DefaultAccessDeniedHandler]).constructor($("security.access.errorPage", "/403.html"))
    bind("security.Filter.authorization", classOf[AuthorizationFilter])
    bind("security.Filter.cas", classOf[CasPreauthFilter])
    bind("web.Interceptor.security", classOf[SecurityInterceptor]).constructor(
      List(ref("security.Filter.cas"), ref("security.Filter.authorization")), ?, ?, ?)

    //realms
    bind("security.Realm.default", classOf[DefaultAccountRealm]).constructor(bean(classOf[RemoteAccountStore]))
    bind("security.Authenticator.realm", classOf[RealmAuthenticator]).constructor(List(ref("security.Realm.default")))

    //session
    bind("security.SessionRegistry.mem", classOf[MemSessionRegistry])
    bind("security.SessionIdPolicy.default", classOf[DefaultSessionIdPolicy])

    //cas
    bind(classOf[CasConfig]).constructor($("security.cas.server"))
    bind("security.TicketValidator.default", classOf[DefaultTicketValidator])

    //authorizer and manager
    bind("security.SecurityManager.default", classOf[DefaultSecurityManager])
    bind("security.Authorizer.remote", classOf[RemoteAuthorizer])
  }

  override def properties: collection.Map[String, String] = {
    val casUrl = Urp.properties.get("security.cas.server").getOrElse(Urp.platformBase + "/cas")
    Map("security.cas.server" -> casUrl)
  }
}
