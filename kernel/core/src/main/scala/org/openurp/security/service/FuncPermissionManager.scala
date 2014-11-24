package org.openurp.security.service

import org.beangle.security.blueprint.FuncResource
import org.beangle.security.blueprint.service.FuncPermissionService
import org.beangle.security.blueprint.Role

trait FuncPermissionManager extends FuncPermissionService {
  
  def activate(resourceId: Array[Integer], active: Boolean): Unit

  def authorize(role: Role, resources: Set[FuncResource])

}