package org.openurp.platform.ids.cas

import java.io.FileInputStream

import org.beangle.cache.ehcache.{ EhCacheChainedManager, EhCacheManager }
import org.beangle.cache.redis.{ JedisPoolFactory, RedisBroadcasterBuilder, RedisCacheManager }
import org.beangle.cache.serializer.FSTSerializer
import org.beangle.commons.collection.Collections
import org.beangle.commons.inject.PropertySource
import org.beangle.commons.inject.bind.AbstractBindModule
import org.beangle.data.jdbc.ds.DataSourceFactory
import org.beangle.ids.cas.id.impl.DefaultServiceTicketIdGenerator
import org.beangle.ids.cas.ticket.impl.CachedTicketRegistry
import org.beangle.ids.cas.web.action.{ LoginAction, LogoutAction, ServiceValidateAction }
import org.beangle.ids.cas.web.helper.DefaultCasSessionIdPolicy
import org.beangle.security.authc.{ DefaultAccountRealm, RealmAuthenticator }
import org.beangle.security.authz.PublicAuthorizer
import org.beangle.security.mgt.DefaultSecurityManager
import org.beangle.security.realm.ldap.{ DefaultCredentialsChecker, DefaultLdapUserService, PoolingContextSource }
import org.beangle.security.session.jdbc.{ DBSessionRegistry, SessionCleaner }
import org.beangle.security.web.UrlEntryPoint
import org.beangle.security.web.access.{ DefaultAccessDeniedHandler, SecurityInterceptor }
import org.openurp.platform.api.app.UrpApp

/**
 * @author chaostone
 */
class DefaultModule extends AbstractBindModule with PropertySource {

  override def binding() {
    // entry point
    bind("security.EntryPoint.url", classOf[UrlEntryPoint]).constructor("/login").primary()
    //interceptor
    bind("security.AccessDeniedHandler.default", classOf[DefaultAccessDeniedHandler]).constructor($("security.access.errorPage", "/403.html"))
    bind("web.Interceptor.security", classOf[SecurityInterceptor])
    //authorizer and manager
    bind("security.SecurityManager.default", classOf[DefaultSecurityManager])
    bind("security.Authorizer.public", PublicAuthorizer)

    bind(classOf[LoginAction])
    bind(classOf[ServiceValidateAction])
    bind(classOf[LogoutAction])
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

class TicketModule extends AbstractBindModule {
  override def binding() {
    bind(classOf[CachedTicketRegistry]).constructor(bean(classOf[RedisCacheManager]).property("ttl", "60"))
    bind(classOf[DefaultServiceTicketIdGenerator])
  }
}

class DbCredentialsModule extends AbstractBindModule {
  override def binding() {
    bind("security.CredentialsChecker.default", classOf[SimpleCredentialsChecker])
      .constructor(ref("DataSource.security"))
  }
}

class LdapCredentialsModule extends AbstractBindModule {
  override def binding() {
    bind("security.ldap.source", classOf[PoolingContextSource])
      .constructor($("ldap.url"), $("ldap.user"), $("ldap.password"))
    bind("security.LdapUserService.default", classOf[DefaultLdapUserService]).constructor(ref("security.ldap.source"), $("ldap.base"))
    bind("security.CredentialsChecker.default", classOf[DefaultCredentialsChecker])
  }
}

class RealmModule extends AbstractBindModule {
  override def binding() {
    bind("DataSource.security", classOf[DataSourceFactory]).property("name", "security")
      .property("url", UrpApp.getUrpAppFile.get.getAbsolutePath)

    bind("security.AccountStore.dao", classOf[DaoUserStore]).constructor(ref("DataSource.security"))
    bind("security.Realm.default", classOf[DefaultAccountRealm])
      .constructor(ref("security.AccountStore.dao"), ref("security.CredentialsChecker.default"))
    bind("security.Authenticator.realm", classOf[RealmAuthenticator])
      .constructor(List(ref("security.Realm.default")))
  }
}

class SessionModule extends AbstractBindModule {
  override def binding() {
    //session registry
    bind("jedis.Factory", classOf[JedisPoolFactory]).constructor(Map("host" -> $("redis.host"), "port" -> $("redis.port")))
    bind("serializer.fst", classOf[FSTSerializer])

    bind("cache.Ehcache", classOf[EhCacheManager]).constructor("ehcache-session", false)

    bind("cache.Chained.session", classOf[EhCacheChainedManager])
      .constructor(ref("cache.Ehcache"), bean(classOf[RedisCacheManager]), true)
      .property("broadcasterBuilder", bean(classOf[RedisBroadcasterBuilder]))

    bind("DataSource.session", classOf[DataSourceFactory]).property("name", "session").property("url", UrpApp.getUrpAppFile.get.getAbsolutePath)

    bind("security.SessionRegistry.db", classOf[DBSessionRegistry])
      .constructor(ref("DataSource.session"), ref("cache.Chained.session"), ref("cache.Ehcache"))
      .property("sessionTable", "session.cas_session_infoes").property("statTable", "session.cas_session_stats")

    bind("security.SessionIdPolicy.cas", classOf[DefaultCasSessionIdPolicy])

    bind(classOf[SessionCleaner])
  }
}

