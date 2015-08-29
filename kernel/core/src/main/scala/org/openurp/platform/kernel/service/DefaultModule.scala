package org.openurp.platform.kernel.service

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.platform.kernel.service.impl.AppDataPermissionManagerImpl
import org.openurp.platform.kernel.service.impl.CachedTokenRepository
import org.beangle.commons.cache.concurrent.ConcurrentMapCacheManager
import org.openurp.platform.kernel.service.impl.DbServiceImpl

class DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[AppDataPermissionManagerImpl])
    bind(classOf[CachedTokenRepository])
    bind(classOf[ConcurrentMapCacheManager])
    bind(classOf[DbServiceImpl])
  }
}