package org.openurp.platform.ids.cas

import java.io.FileInputStream
import org.beangle.cache.composite.CompositeCacheManager
import org.beangle.cache.ehcache.EhCacheManager
import org.beangle.cache.redis.{ JedisPoolFactory, RedisBroadcasterBuilder, RedisCacheManager }
import org.beangle.cache.serializer.FSTSerializer
import org.beangle.commons.collection.Collections
import org.beangle.commons.inject.PropertySource
import org.beangle.commons.inject.bind.AbstractBindModule
import org.beangle.data.jdbc.ds.DataSourceFactory
import org.beangle.ids.cas.id.impl.DefaultServiceTicketIdGenerator
import org.beangle.ids.cas.ticket.impl.CachedTicketRegistry
import org.beangle.ids.cas.web.helper.DefaultCasSessionIdPolicy
import org.beangle.security.authc.{ DefaultAccountRealm, RealmAuthenticator }
import org.beangle.security.authz.PublicAuthorizer
import org.beangle.security.mgt.DefaultSecurityManager
import org.beangle.security.session.jdbc.{ DBSessionRegistry, SessionCleaner }
import org.beangle.security.web.UrlEntryPoint
import org.beangle.security.web.access.{ DefaultAccessDeniedHandler, SecurityInterceptor }
import org.beangle.cache.composite.CompositeCacheManager
import org.beangle.cache.ehcache.EhCacheManager
import org.beangle.cache.redis.JedisPoolFactory
import org.beangle.cache.redis.RedisBroadcasterBuilder
import org.beangle.cache.redis.RedisCacheManager
import org.beangle.cache.serializer.FSTSerializer
import org.beangle.data.jdbc.ds.DataSourceFactory
import org.beangle.ids.cas.id.impl.DefaultServiceTicketIdGenerator
import org.beangle.ids.cas.ticket.impl.CachedTicketRegistry
import org.beangle.ids.cas.web.helper.DefaultCasSessionIdPolicy
import org.beangle.security.authc.DefaultAccountRealm
import org.beangle.security.authc.RealmAuthenticator
import org.beangle.security.mgt.DefaultSecurityManager
import org.beangle.security.session.jdbc.DBSessionRegistry
import org.beangle.security.session.jdbc.SessionCleaner
import org.beangle.security.web.UrlEntryPoint
import org.beangle.security.web.access.DefaultAccessDeniedHandler
import org.beangle.security.web.access.SecurityInterceptor

/**
 * @author chaostone
 */
class DefaultModule extends AbstractBindModule with PropertySource {

  val url = System.getProperty("APP_CONFIG")

  override def binding() {
    // entry point
    bind("security.EntryPoint.url", classOf[UrlEntryPoint]).constructor("/login").primary()

    //interceptor
    bind("security.AccessDeniedHandler.default", classOf[DefaultAccessDeniedHandler]).constructor($("security.access.errorPage", "/403.html"))
    bind("web.Interceptor.security", classOf[SecurityInterceptor])

    // dao realm
    bind("security.CredentialsChecker.default", classOf[SimpleCredentialsChecker])
    bind("security.AccountStore.dao", classOf[DaoUserStore])
    bind("security.Realm.default", classOf[DefaultAccountRealm])
      .constructor(ref("security.AccountStore.dao"), ref("security.CredentialsChecker.default"))
    bind("security.Authenticator.realm", classOf[RealmAuthenticator])
      .constructor(List(ref("security.Realm.default")))

    //authorizer and manager
    bind("security.SecurityManager.default", classOf[DefaultSecurityManager])
    bind("security.Authorizer.public", PublicAuthorizer)

    bind("jedis.Factory", classOf[JedisPoolFactory]).constructor(Map("host" -> $("redis.host"), "port" -> $("redis.port")))
    bind("serializer.fst", classOf[FSTSerializer])

    //session
    bind("cache.Ehcache", classOf[EhCacheManager])
      .property("broadcasterBuilder", bean(classOf[RedisBroadcasterBuilder]))
      .property("name", "session-ehcache")

    bind("cache.Composite.session", classOf[CompositeCacheManager]).constructor(ref("cache.Ehcache"), bean(classOf[RedisCacheManager]))

    bind("DataSource.security", classOf[DataSourceFactory]).property("name", "default").property("url", url)

    bind("security.SessionRegistry.db", classOf[DBSessionRegistry])
      .constructor(ref("DataSource.security"), ref("cache.Composite.session"), ref("cache.Ehcache"))
      .property("sessionTable", "cas_session_infoes").property("statTable", "cas_session_stats")

    bind("security.SessionIdPolicy.cas", classOf[DefaultCasSessionIdPolicy])
    bind(classOf[SessionCleaner])

    //ticket
    bind(classOf[CachedTicketRegistry]).constructor(bean(classOf[RedisCacheManager]).property("ttl", "60"))
    bind(classOf[DefaultServiceTicketIdGenerator])
  }

  override def properties: collection.Map[String, String] = {
    if (null == url) throw new RuntimeException("Cannot find system properties APP_CONFIG")
    val is = new FileInputStream(url)
    val datas = Collections.newMap[String, String]
    val app = scala.xml.XML.load(is)
    (app \\ "redis") foreach { e =>
      datas += ("redis.host" -> (e \\ "host").text.trim)
      datas += ("redis.port" -> (e \\ "port").text.trim)
    }
    datas.toMap
  }
}