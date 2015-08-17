package org.openurp.platform.kernel.app.service

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.platform.kernel.app.service.impl.AppFuncPermissionManagerImpl
import org.openurp.platform.kernel.app.service.impl.CachedTokenRepository
import org.beangle.commons.cache.concurrent.ConcurrentMapCacheManager

class DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[AppFuncPermissionManagerImpl])
    bind(classOf[CachedTokenRepository])
    bind(classOf[ConcurrentMapCacheManager])
  }
}