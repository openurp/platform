package org.openurp.security

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.security.service.internal.UserManagerImpl
import org.openurp.security.service.internal.ProfileServiceImpl
import org.openurp.security.service.internal.RoleManagerImpl
import org.openurp.security.service.internal.FuncPermissionManagerImpl

class DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[UserManagerImpl], classOf[RoleManagerImpl])
    bind(classOf[ProfileServiceImpl],classOf[FuncPermissionManagerImpl])
  }
}