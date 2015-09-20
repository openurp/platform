package org.openurp.platform.kernel.service


trait AppDataPermissionManager  {
  
  def activate(resourceId: Iterable[Int], active: Boolean): Unit

}