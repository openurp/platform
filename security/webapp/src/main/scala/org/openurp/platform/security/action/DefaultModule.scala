package org.openurp.platform.security.action

import org.beangle.commons.inject.bind.AbstractBindModule

class DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[AccountAction], classOf[DashboardAction])
    bind(classOf[DimensionAction], classOf[PermissionAction], classOf[UserAction], classOf[RoleAction], classOf[ProfileAction])
    bind(classOf[FuncResourceAction])
    bind(classOf[IndexAction])
  }
}