package org.openurp.platform.kernel.app.service

import org.beangle.security.blueprint.FuncResource
import org.beangle.security.blueprint.service.FuncPermissionService
import org.beangle.security.blueprint.Role

trait AppFuncPermissionManager  {
  
  def activate(resourceId: Array[Integer], active: Boolean): Unit

}