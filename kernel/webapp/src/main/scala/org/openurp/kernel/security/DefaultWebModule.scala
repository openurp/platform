package org.openurp.kernel.security

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.kernel.security.action.{ FieldAction, IndexAction, PermissionAction, ProfileAction, RoleAction, UserAction }
import org.openurp.kernel.security.action.AccountAction

class DefaultWebModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[AccountAction], classOf[IndexAction], classOf[FieldAction], classOf[PermissionAction], classOf[UserAction], classOf[RoleAction],
      classOf[ProfileAction])
  }
}