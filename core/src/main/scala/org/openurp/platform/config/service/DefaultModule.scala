package org.openurp.platform.config.service

import org.beangle.cdi.bind.BindModule
import org.openurp.platform.config.service.impl.AppDataPermissionManagerImpl
import org.openurp.platform.config.service.impl.DbServiceImpl
import org.openurp.platform.config.service.impl.AppServiceImpl

class DefaultModule extends BindModule {

  protected override def binding() {
    bind(classOf[AppDataPermissionManagerImpl])
    bind(classOf[DbServiceImpl])
    bind(classOf[AppServiceImpl])
  }
}