package org.openurp.platform.web

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.platform.web.action.IndexAction
import org.openurp.platform.web.action.security.{ DashboardAction, DataPermissionAction, DataResourceAction, FuncResourceAction, MenuAction, PermissionAction }
import org.openurp.platform.web.action.user.{ AccountAction, DimensionAction, RoleAction, UserAction, ProfileAction }
import org.beangle.commons.cache.concurrent.ConcurrentMapCacheManager

class SecurityModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[AccountAction], classOf[DashboardAction])
    bind(classOf[DimensionAction], classOf[PermissionAction], classOf[UserAction], classOf[RoleAction], classOf[ProfileAction])
    bind(classOf[FuncResourceAction], classOf[MenuAction])
    bind(classOf[IndexAction])
    bind(classOf[DataPermissionAction])
    bind(classOf[DataResourceAction])
  }
}
