package org.openurp.platform.ws

import org.beangle.commons.cache.concurrent.ConcurrentMapCacheManager
import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.platform.oauth.service.impl.MemTokenRepository
import org.openurp.platform.ws.config.DatasourceWS
import org.openurp.platform.ws.oauth.TokenWS
import org.openurp.platform.ws.security.{ AppWS, DataPermissionWS, DataResourceWS, FuncPermissionWS, FuncResourceWS, MenuProfileWS, MenuWS, RootWS }
import org.openurp.platform.ws.user.{ AccountWS, DimensionWS, ProfileWS }

class DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[DatasourceWS])
    bind(classOf[TokenWS])

    bind(classOf[MenuWS], classOf[MenuProfileWS])
    bind(classOf[FuncResourceWS], classOf[DataResourceWS])
    bind(classOf[AccountWS], classOf[AppWS])
    bind(classOf[RootWS], classOf[FuncPermissionWS])

    bind(classOf[DimensionWS], classOf[DataPermissionWS], classOf[ProfileWS])

    bind(classOf[MemTokenRepository], classOf[ConcurrentMapCacheManager])
  }
}
