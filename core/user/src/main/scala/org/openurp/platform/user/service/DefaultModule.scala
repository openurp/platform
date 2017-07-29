package org.openurp.platform.user.service

import org.beangle.cdi.bind.BindModule
import org.openurp.platform.user.service.impl.{ RoleServiceImpl, UserServiceImpl }

class DefaultModule extends BindModule{
  
  override def binding() {
    bind(classOf[UserServiceImpl])
    bind(classOf[RoleServiceImpl])
  }
}