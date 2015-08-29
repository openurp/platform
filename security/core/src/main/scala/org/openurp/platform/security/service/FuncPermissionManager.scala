package org.openurp.platform.security.service

import org.openurp.platform.security.model.FuncResource
import org.openurp.platform.security.model.Role

trait FuncPermissionManager extends FuncPermissionService {
  
  def activate(resourceId: Array[Integer], active: Boolean): Unit

  def authorize(role: Role, resources: Set[FuncResource])

}