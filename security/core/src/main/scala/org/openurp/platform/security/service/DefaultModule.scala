package org.openurp.platform.security.service

import org.beangle.commons.inject.bind.AbstractBindModule
import org.beangle.data.jdbc.query.JdbcExecutor
import org.beangle.security.authc.RealmAuthenticator
import org.beangle.security.mgt.DefaultSecurityManager
import org.beangle.security.realm.cas.{ CasConfig, CasEntryPoint, CasPreauthFilter, DefaultCasRealm, DefaultTicketValidator }
import org.beangle.security.session.{ DefaultSessionBuilder, SessionCleaner }
import org.beangle.security.web.access.{ AuthorizationFilter, DefaultAccessDeniedHandler, SecurityInterceptor }
import org.beangle.security.web.session.DefaultSessionIdPolicy
import org.openurp.platform.security.service.impl.{ CachedDaoAuthorizer, DaoUserStore, FuncPermissionManagerImpl, MenuServiceImpl, ProfileServiceImpl, RoleManagerImpl, SessionProfileProvider, UserManagerImpl }

class DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[UserManagerImpl], classOf[RoleManagerImpl])
    bind(classOf[MenuServiceImpl])
    bind(classOf[ProfileServiceImpl], classOf[FuncPermissionManagerImpl])
  }
}