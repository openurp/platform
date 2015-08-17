package org.openurp.platform.security.auth.action

import org.beangle.commons.inject.bind.AbstractBindModule

class DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind( classOf[FieldAction], classOf[PermissionAction], classOf[UserAction], classOf[RoleAction],
      classOf[ProfileAction])
  }
}