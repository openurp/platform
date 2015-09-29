package org.openurp.platform.kernel.ws

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.platform.kernel.service.impl.MemTokenRepository
import org.beangle.commons.cache.concurrent.ConcurrentMapCacheManager

class DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[DatasourceWS])
    bind(classOf[TokenWS])
    
    bind(classOf[MemTokenRepository],classOf[ConcurrentMapCacheManager])
  }
}