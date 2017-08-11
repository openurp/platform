package org.openurp.platform.ids.cas

import java.io.FileInputStream

import org.beangle.cache.caffeine.CaffeineCacheManager
import org.beangle.cache.redis.{ JedisPoolFactory, RedisCacheManager }
import org.beangle.cdi.PropertySource
import org.beangle.cdi.bind.BindModule
import org.beangle.commons.collection.Collections
import org.beangle.commons.io.DefaultBinarySerializer
import org.beangle.commons.lang.Strings
import org.beangle.data.jdbc.ds.DataSourceFactory
import org.beangle.ids.cas.id.impl.DefaultServiceTicketIdGenerator
import org.beangle.ids.cas.ticket.impl.CachedTicketRegistry
import org.beangle.ids.cas.web.action.{ LoginAction, LogoutAction, ServiceValidateAction }
import org.beangle.security.authc.{ DefaultAccountRealm, RealmAuthenticator }
import org.beangle.security.authz.PublicAuthorizer
import org.beangle.security.realm.ldap.{ DefaultCredentialsChecker, PoolingContextSource, SimpleLdapUserStore }
import org.beangle.security.session.jdbc.DBSessionRegistry
import org.beangle.security.web.{ UrlEntryPoint, WebSecurityManager }
import org.beangle.security.web.access.{ DefaultAccessDeniedHandler, SecurityInterceptor }
import org.openurp.platform.api.Urp
import org.openurp.platform.api.app.UrpApp
import org.openurp.platform.api.security.DefaultUrpSessionIdPolicy
import org.openurp.platform.user.service.impl.DaoUserStore
import org.beangle.ids.cas.cache.CasCacheService

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

class TicketModule extends BindModule {
  override def binding() {
    bind("jedis.Factory", classOf[JedisPoolFactory]).constructor(Map("host" -> $("redis.host"), "port" -> $("redis.port")))
    bind("serializer.default", DefaultBinarySerializer)
    bind(classOf[CachedTicketRegistry]).constructor(bean(classOf[RedisCacheManager]).property("ttl", "60"))
    bind(classOf[DefaultServiceTicketIdGenerator])
  }
}

class DbCredentialsModule extends BindModule {
  override def binding() {
    bind("security.CredentialsChecker.default", classOf[DaoCredentialsChecker])
      .constructor(ref("DataSource.security"))
  }
}

class LdapCredentialsModule extends BindModule {
  override def binding() {
    bind("security.ldap.source", classOf[PoolingContextSource])
      .constructor($("ldap.url"), $("ldap.user"), $("ldap.password"))
    bind("security.LdapUserStore.default", classOf[SimpleLdapUserStore])
      .constructor(ref("security.ldap.source"), $("ldap.base"))
    bind("security.CredentialsChecker.default", classOf[DefaultCredentialsChecker])
  }
}

class DaoRealmModule extends BindModule {
  override def binding() {
    bind("DataSource.security", classOf[DataSourceFactory]).property("name", "security")
      .property("url", UrpApp.getUrpAppFile.get.getAbsolutePath).primary()

    bind("security.Realm.default", classOf[DefaultAccountRealm])
      .constructor(bean(classOf[DaoUserStore]), ref("security.CredentialsChecker.default"))
    bind("security.Authenticator", classOf[RealmAuthenticator])
      .constructor(List(ref("security.Realm.default")))
  }
}

class SessionModule extends BindModule {
  override def binding() {
    bind("cache.Caffeine", classOf[CaffeineCacheManager])
    //.constructor("ehcache-session", false)
    bind("DataSource.session", classOf[DataSourceFactory])
      .property("name", "session")
      .property("url", UrpApp.getUrpAppFile.get.getAbsolutePath)

    bind("security.SessionRegistry.db", classOf[DBSessionRegistry])
      .constructor(ref("DataSource.session"), ref("cache.Caffeine"))
      .property("sessionTable", "session.session_infoes")

    bind("security.SessionIdPolicy.urp", classOf[DefaultUrpSessionIdPolicy])
      .property("path", "/").property("domain", Strings.substringAfter(Urp.base, "//"))
  }
}

class WebModule extends BindModule {
  override def binding() {
    bind(classOf[CasCacheService]).constructor(ref("cache.Caffeine"))
    bind(classOf[LoginAction])
    bind(classOf[ServiceValidateAction])
    bind(classOf[LogoutAction])
  }
}