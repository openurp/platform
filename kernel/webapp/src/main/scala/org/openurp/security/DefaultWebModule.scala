package org.openurp.security

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.security.action.{ FieldAction, IndexAction, PermissionAction, ProfileAction, RoleAction, UserAction }
import org.openurp.security.action.AccountAction

class DefaultWebModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[AccountAction], classOf[IndexAction], classOf[FieldAction], classOf[PermissionAction], classOf[UserAction], classOf[RoleAction],
      classOf[ProfileAction])
  }
}