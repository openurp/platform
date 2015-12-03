package org.openurp.platform.ws

import org.beangle.commons.cache.concurrent.ConcurrentMapCacheManager
import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.platform.oauth.service.impl.MemTokenRepository
import org.openurp.platform.ws.config.DatasourceWS
import org.openurp.platform.ws.oauth.TokenWS
import org.openurp.platform.ws.security.{ AppWS, DataResourceWS, FuncResourceWS, MenuProfileWS, MenuWS }
import org.openurp.platform.ws.user.AccountWS

class DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[DatasourceWS])
    bind(classOf[TokenWS])

    bind(classOf[MenuWS], classOf[MenuProfileWS])
    bind(classOf[FuncResourceWS], classOf[DataResourceWS])
    bind(classOf[AccountWS], classOf[AppWS])

    bind(classOf[MemTokenRepository], classOf[ConcurrentMapCacheManager])
  }
}