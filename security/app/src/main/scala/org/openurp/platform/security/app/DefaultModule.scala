package org.openurp.platform.security.app

import org.beangle.commons.cache.concurrent.ConcurrentMapCacheManager
import org.beangle.commons.inject.bind.AbstractBindModule

object DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind("security.Realm.remoteApp", classOf[RemoteAppRealm])
    bind("security.Realm.remoteUser", classOf[RemoteUserRealm])
    bind("security.Authorizer.remoteApp", classOf[RemoteAuthorizer])
    bind("security.Filter.appPreauth", classOf[TokenPreauthFilter])
    bind(classOf[ConcurrentMapCacheManager])
  }
}