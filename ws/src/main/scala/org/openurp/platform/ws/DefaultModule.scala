package org.openurp.platform.ws

import org.beangle.cache.concurrent.ConcurrentMapCacheManager
import org.beangle.cdi.bind.BindModule
import org.openurp.platform.oauth.service.impl.MemTokenRepository
import org.openurp.platform.ws.config.DatasourceWS
import org.openurp.platform.ws.oauth.TokenWS
import org.openurp.platform.ws.user.AppWS
import org.openurp.platform.ws.user.RootWS
import org.openurp.platform.ws.user.{ AccountWS, DimensionWS, ProfileWS }
import org.openurp.platform.ws.security.{ func, data }

class DefaultModule extends BindModule {

  protected override def binding() {
    bind(classOf[DatasourceWS])
    bind(classOf[TokenWS])

    bind(classOf[func.MenuWS])
    bind(classOf[func.ResourceWS], classOf[func.PermissionWS])
    bind(classOf[data.PermissionWS], classOf[data.ResourceWS])

    bind(classOf[AccountWS], classOf[AppWS], classOf[DimensionWS])
    bind(classOf[RootWS], classOf[ProfileWS])

    bind(classOf[MemTokenRepository], classOf[ConcurrentMapCacheManager])
  }
}
