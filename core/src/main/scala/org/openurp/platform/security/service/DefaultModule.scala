package org.openurp.platform.security.service

import org.beangle.cdi.bind.BindModule
import org.openurp.platform.user.service.impl.UserServiceImpl
import org.openurp.platform.user.service.impl.RoleServiceImpl
import org.openurp.platform.security.service.impl.ProfileServiceImpl
import org.openurp.platform.security.service.impl.FuncPermissionServiceImpl
import org.openurp.platform.security.service.impl.MenuServiceImpl

class DefaultModule extends BindModule{
  
  override def binding() {
    bind(classOf[FuncPermissionServiceImpl])
    bind(classOf[MenuServiceImpl])
    bind(classOf[ProfileServiceImpl])
  }
}