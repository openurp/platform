package org.openurp.kernel.security

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.kernel.security.service.internal.UserManagerImpl
import org.openurp.kernel.security.service.internal.ProfileServiceImpl
import org.openurp.kernel.security.service.internal.RoleManagerImpl
import org.openurp.kernel.security.service.internal.FuncPermissionManagerImpl
import org.openurp.kernel.security.service.internal.CachedDaoAuthorizer
import org.openurp.kernel.app.auth.service.impl.CachedTokenRepository
import org.beangle.commons.cache.concurrent.ConcurrentMapCacheManager

class DefaultServiceModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[UserManagerImpl], classOf[RoleManagerImpl])
    bind(classOf[ProfileServiceImpl],classOf[FuncPermissionManagerImpl])
//    bind(classOf[CachedDaoAuthorizer])
    bind(classOf[CachedTokenRepository])
    bind(classOf[ConcurrentMapCacheManager])
  }
}