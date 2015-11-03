package org.openurp.platform.api.cas

import org.beangle.commons.inject.PropertySource
import org.beangle.commons.inject.bind.AbstractBindModule
import org.beangle.data.jdbc.query.JdbcExecutor
import org.beangle.security.authc.RealmAuthenticator
import org.beangle.security.mgt.DefaultSecurityManager
import org.beangle.security.realm.cas.{ CasConfig, CasEntryPoint, CasPreauthFilter, DefaultCasRealm, DefaultTicketValidator }
import org.beangle.security.session.{ DefaultSessionBuilder, SessionCleaner }
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
    bind("security.AccountStore.remote", classOf[RemoteAccountStore])
    bind("security.Realm.cas", classOf[DefaultCasRealm])
    bind("security.Authenticator.realm", classOf[RealmAuthenticator]).constructor(List(ref("security.Realm.cas")))

    //session
    bind("security.SessionRegistry.mem", classOf[MemSessionRegistry])
    bind("security.SessionIdPolicy.default", classOf[DefaultSessionIdPolicy])
    bind("security.SessionRegistry.app", classOf[AppDBSessionRegistry]).primary()
    bind(classOf[JdbcExecutor])
    bind(classOf[SessionCleaner])
    bind("security.ProfileProvider.remote", classOf[RemoteProfileProvider])
    //    bind("security.ProfileProvider.default", classOf[DefaultProfileProvider])
    bind("security.SessionBuilder.default", classOf[DefaultSessionBuilder])

    //cas
    bind(classOf[CasConfig]).constructor($("security.cas.server"))
    bind("security.TicketValidator.default", classOf[DefaultTicketValidator])

    //authorizer and manager
    bind("security.SecurityManager.default", classOf[DefaultSecurityManager])
    bind("security.Authorizer.remote", classOf[RemoteAuthorizer])
    //    bind("security.Authorizer.dao", classOf[CachedDaoAuthorizer])
  }

  override def properties: collection.Map[String, String] = {
    val casUrl = Urp.properties.get("security.cas.server").getOrElse(Urp.platformBase + "/cas")
    Map("security.cas.server" -> casUrl)
  }
}
