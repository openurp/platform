package org.openurp.platform.kernel.service


trait AppDataPermissionManager  {
  
  def activate(resourceId: Array[Integer], active: Boolean): Unit

}