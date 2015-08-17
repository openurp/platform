package org.openurp.platform.security.service

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.platform.security.service.internal.{FuncPermissionManagerImpl, ProfileServiceImpl, RoleManagerImpl, UserManagerImpl}

class DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[UserManagerImpl], classOf[RoleManagerImpl])
    bind(classOf[ProfileServiceImpl], classOf[FuncPermissionManagerImpl])
    //    bind(classOf[CachedDaoAuthorizer])
  }
}