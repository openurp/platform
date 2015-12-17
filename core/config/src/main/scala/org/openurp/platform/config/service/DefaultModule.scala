package org.openurp.platform.config.service

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.platform.config.service.impl.AppDataPermissionManagerImpl
import org.openurp.platform.config.service.impl.DbServiceImpl
import org.openurp.platform.config.service.impl.AppServiceImpl

class DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[AppDataPermissionManagerImpl])
    bind(classOf[DbServiceImpl])
    bind(classOf[AppServiceImpl])
  }
}