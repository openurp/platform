package org.openurp.platform.ws

import org.beangle.cdi.bind.BindModule
import org.openurp.platform.security.service.impl.{ FuncPermissionServiceImpl, MenuServiceImpl, ProfileServiceImpl }
import org.openurp.platform.user.service.impl.{ RoleServiceImpl, UserServiceImpl }

class ServiceModule extends BindModule {

  protected override def binding() {
    bind(classOf[UserServiceImpl])
    bind(classOf[RoleServiceImpl])

    bind(classOf[FuncPermissionServiceImpl])
    bind(classOf[MenuServiceImpl])
    bind(classOf[ProfileServiceImpl])

  }
}