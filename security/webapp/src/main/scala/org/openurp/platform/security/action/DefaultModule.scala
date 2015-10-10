package org.openurp.platform.security.action

import org.beangle.commons.inject.PropertySource
import org.beangle.commons.inject.bind.AbstractBindModule

class DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[AccountAction], classOf[DashboardAction])
    bind(classOf[DimensionAction], classOf[PermissionAction], classOf[UserAction], classOf[RoleAction], classOf[ProfileAction])
    bind(classOf[FuncResourceAction], classOf[MenuAction])
    bind(classOf[IndexAction])
    bind(classOf[LoginAction])
    bind(classOf[SessionProfileAction])
    bind(classOf[DataPermissionAction])
    bind(classOf[DataResourceAction])
  }

}

class DefaultSecurityModule extends AbstractBindModule with PropertySource {
  protected override def binding() {}
  def properties: collection.Map[String, String] = {
    Map("security.login.url" -> "/security/login")
  }
}