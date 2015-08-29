package org.openurp.platform.security.service

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.platform.security.service.impl.{ FuncPermissionManagerImpl, ProfileServiceImpl, RoleManagerImpl, UserManagerImpl }
import org.openurp.platform.security.service.impl.DaoUserRealm
import org.openurp.platform.security.service.impl.DaoUserStore
import org.openurp.platform.security.service.impl.CachedDaoAuthorizer
import org.beangle.security.session.jdbc.DBSessionRegistry
import org.beangle.data.jdbc.query.JdbcExecutor
import org.beangle.security.session.DefaultSessionBuilder
import org.openurp.platform.security.service.impl.MenuServiceImpl

class DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[UserManagerImpl], classOf[RoleManagerImpl])
    bind(classOf[MenuServiceImpl])
    bind(classOf[ProfileServiceImpl], classOf[FuncPermissionManagerImpl])
    //登录和权限检查
    bind("security.Realm.dao", classOf[DaoUserRealm])
    bind(classOf[DaoUserStore])
    bind("security.Authorizer.dao", classOf[CachedDaoAuthorizer])
//    bind(classOf[DBSessionRegistry]).primary()
//    bind(classOf[JdbcExecutor])
//    bind(classOf[DefaultSessionBuilder])
  }
}