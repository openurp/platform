package org.openurp.platform.api.cas

import org.beangle.cache.caffeine.CaffeineCacheManager
import org.beangle.cdi.PropertySource
import org.beangle.cdi.bind.BindModule
import org.beangle.commons.collection.Collections
import org.beangle.commons.lang.Strings
import org.beangle.security.authc.{ DefaultAccount, RealmAuthenticator }
import org.beangle.security.protobuf.{ AccountSerializer, SessionSerializer }
import org.beangle.security.realm.cas.{ CasConfig, CasEntryPoint }
import org.beangle.security.session.DefaultSession
import org.beangle.security.web.access.{ AuthorizationFilter, DefaultAccessDeniedHandler, SecurityInterceptor }
import org.beangle.serializer.protobuf.ProtobufSerializer
import org.openurp.platform.api.Urp
import org.openurp.platform.api.security.{ DefaultUrpSessionIdPolicy, RemoteAuthorizer }

class DefaultModule extends BindModule with PropertySource {

  override def binding() {
    // entry point
    bind("security.EntryPoint.cas", classOf[CasEntryPoint]).primary()

    //interceptor and filters
    bind("security.AccessDeniedHandler.default", classOf[DefaultAccessDeniedHandler])
      .constructor($("security.access.errorPage", "/403.html"))
    bind("security.Filter.authorization", classOf[AuthorizationFilter])
    bind("web.Interceptor.security", classOf[SecurityInterceptor]).constructor(
      List(ref("security.Filter.authorization")), ?, ?, ?)

    bind("security.Authenticator", classOf[RealmAuthenticator])

    bind("cache.Caffeine", classOf[CaffeineCacheManager]).constructor(true)

    val protobuf = new ProtobufSerializer
    protobuf.register(classOf[DefaultSession], SessionSerializer)
    protobuf.register(classOf[DefaultAccount], AccountSerializer)

    bind("security.SessionRepo.http", classOf[CasHttpSessionRepo])
      .constructor(ref("casConfig"), ref("cache.Caffeine"), protobuf)

    bind("security.SessionIdPolicy.cookie", classOf[DefaultUrpSessionIdPolicy])
      .property("path", "/").property("domain", Strings.substringAfter(Urp.base, "//"))

    //cas
    bind("casConfig", classOf[CasConfig]).constructor($("openurp.platform.cas.server"))

    //authorizer and manager
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