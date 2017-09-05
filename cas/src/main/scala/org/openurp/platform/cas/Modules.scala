package org.openurp.platform.cas

import java.io.FileInputStream

import org.beangle.cache.caffeine.CaffeineCacheManager
import org.beangle.cache.redis.JedisPoolFactory
import org.beangle.cdi.PropertySource
import org.beangle.cdi.bind.BindModule
import org.beangle.commons.collection.Collections
import org.beangle.commons.lang.Strings
import org.beangle.data.jdbc.ds.DataSourceFactory
import org.beangle.ids.cas.id.impl.DefaultServiceTicketIdGenerator
import org.beangle.ids.cas.ticket.{ DefaultTicketCacheService, DefaultTicketRegistry }
import org.beangle.ids.cas.web.action.{ LoginAction, LogoutAction, ServiceValidateAction, SessionAction }
import org.beangle.security.authc.{ DefaultAccount, DefaultAccountRealm, RealmAuthenticator }
import org.beangle.security.authz.PublicAuthorizer
import org.beangle.security.protobuf.{ AccountSerializer, SessionSerializer }
import org.beangle.security.realm.ldap.{ DefaultCredentialsChecker, PoolingContextSource, SimpleLdapUserStore }
import org.beangle.security.session.DefaultSession
import org.beangle.security.session.jdbc.DBSessionRegistry
import org.beangle.security.web.{ UrlEntryPoint, WebSecurityManager }
import org.beangle.security.web.access.{ DefaultAccessDeniedHandler, SecurityInterceptor }
import org.beangle.serializer.protobuf.ProtobufSerializer
import org.openurp.app.UrpApp
import org.openurp.app.Urp
import org.openurp.platform.cas.service.DaoAccountStore
import org.openurp.platform.cas.service.DefaultUrpSessionIdPolicy
import org.beangle.ids.cas.service.DaoCredentialsChecker

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
    bind(classOf[DefaultTicketCacheService]).constructor(ref("jedis.Factory"))
    bind(classOf[DefaultTicketRegistry])
    bind(classOf[DefaultServiceTicketIdGenerator])
  }
}

class DbCredentialsModule extends BindModule {
  override def binding() {
    bind("security.CredentialsChecker.default", classOf[DaoCredentialsChecker])
      .constructor(ref("DataSource.security"), "select password from usr.users where code=?")
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
      .constructor(bean(classOf[DaoAccountStore]), ref("security.CredentialsChecker.default"))
    bind("security.Authenticator", classOf[RealmAuthenticator])
      .constructor(List(ref("security.Realm.default")))
  }
}

class SessionModule extends BindModule {
  override def binding() {
    bind("cache.Caffeine", classOf[CaffeineCacheManager]).constructor(true)
    bind("DataSource.session", classOf[DataSourceFactory])
      .property("name", "session")
      .property("url", UrpApp.getUrpAppFile.get.getAbsolutePath)

    val protobuf = new ProtobufSerializer
    protobuf.register(classOf[DefaultSession], SessionSerializer)
    protobuf.register(classOf[DefaultAccount], AccountSerializer)

    bind("Serializer.protobuf", protobuf)
    bind("security.SessionRegistry.db", classOf[DBSessionRegistry])
      .constructor(ref("DataSource.session"), ref("cache.Caffeine"), protobuf)
      .property("sessionTable", "session.session_infoes")

    bind("security.SessionIdPolicy.urp", classOf[DefaultUrpSessionIdPolicy])
      .property("path", "/").property("domain", Strings.substringAfter(Urp.base, "//"))
  }
}

class WebModule extends BindModule {
  override def binding() {
    bind(classOf[LoginAction])
    bind(classOf[ServiceValidateAction])
    bind(classOf[LogoutAction])
    bind(classOf[SessionAction])
  }
}