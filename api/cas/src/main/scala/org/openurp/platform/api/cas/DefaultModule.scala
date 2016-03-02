package org.openurp.platform.api.cas

import java.io.{ File, FileInputStream }

import org.beangle.cache.ehcache.{ EhCacheChainedManager, EhCacheManager }
import org.beangle.cache.redis.{ JedisPoolFactory, RedisBroadcasterBuilder, RedisCacheManager }
import org.beangle.cache.serializer.FSTSerializer
import org.beangle.commons.collection.Collections
import org.beangle.commons.inject.PropertySource
import org.beangle.commons.inject.bind.AbstractBindModule
import org.beangle.data.jdbc.ds.DataSourceFactory
import org.beangle.security.authc.{ DefaultAccountRealm, RealmAuthenticator }
import org.beangle.security.mgt.DefaultSecurityManager
import org.beangle.security.realm.cas.{ CasConfig, CasEntryPoint, CasPreauthFilter, DefaultTicketValidator }
import org.beangle.security.session.jdbc.DBSessionRegistry
import org.beangle.security.web.access.{ AuthorizationFilter, DefaultAccessDeniedHandler, SecurityInterceptor }
import org.beangle.security.web.session.CookieSessionIdPolicy
import org.openurp.platform.api.Urp

class DefaultModule extends AbstractBindModule with PropertySource {

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
    bind("security.Authenticator.realm", classOf[RealmAuthenticator]).constructor(List(ref("security.Realm.default")))

    //session
    bind("jedis.Factory", classOf[JedisPoolFactory]).constructor(Map("host" -> $("redis.host"), "port" -> $("redis.port"), "database" -> "1"))
    bind("serializer.fst", classOf[FSTSerializer])

    bind("DataSource.session#", classOf[DataSourceFactory]).property("name", "session").property("url", Urp.home + "/platform/session.xml")

    bind("cache.Ehcache", classOf[EhCacheManager]).constructor("ehcache-security", false)

    bind("cache.Chained.session", classOf[EhCacheChainedManager])
      .constructor(ref("cache.Ehcache"), bean(classOf[RedisCacheManager]), true)
      .property("broadcasterBuilder", bean(classOf[RedisBroadcasterBuilder]))
      .property("propagateExpiration", false)

    bind("security.SessionRegistry.db", classOf[DBSessionRegistry])
      .constructor(ref("DataSource.session#"), ref("cache.Chained.session"), ref("cache.Ehcache"))
      .property("sessionTable", "app_session_infoes").property("statTable", "app_session_stats")

    bind("security.SessionIdPolicy.cookie", classOf[DefaultUrpSessionIdPolicy]).property("path", "/")

    //cas
    bind(classOf[CasConfig]).constructor($("openurp.platform.cas.server"))
    bind("security.TicketValidator.default", classOf[DefaultTicketValidator])

    //authorizer and manager
    bind("security.SecurityManager.default", classOf[DefaultSecurityManager])
    bind("security.Authorizer.remote", classOf[RemoteAuthorizer]).constructor(ref("cache.Ehcache"))
  }

  override def properties: collection.Map[String, String] = {
    val datas = Collections.newMap[String, String]
    var casUrl = Urp.properties.get("openurp.platform.cas.server").getOrElse(Urp.platformBase + "/cas")
    if (!casUrl.startsWith("http")) casUrl = "http://" + casUrl
    datas += ("openurp.platform.cas.server" -> casUrl)
    val is = new FileInputStream(new File(Urp.home + "/platform/session.xml"))
    val app = scala.xml.XML.load(is)
    (app \\ "redis") foreach { e =>
      datas += ("redis.host" -> (e \\ "host").text.trim)
      datas += ("redis.port" -> (e \\ "port").text.trim)
    }
    is.close()
    datas.toMap
  }
}