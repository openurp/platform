package org.openurp.platform.security.app

import org.beangle.commons.cache.concurrent.ConcurrentMapCacheManager
import org.beangle.commons.inject.bind.AbstractBindModule

class DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind("security.Realm.remoteApp", classOf[RemoteAppRealm])
    bind("security.Authorizer.remoteApp", classOf[RemoteAppAuthorizer])
    bind("security.Filter.appPreauth", classOf[AppPreauthFilter])
    bind(classOf[ConcurrentMapCacheManager])
  }
}