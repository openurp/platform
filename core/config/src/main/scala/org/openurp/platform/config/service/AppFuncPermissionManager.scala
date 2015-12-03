package org.openurp.platform.config.service


trait AppDataPermissionManager  {
  
  def activate(resourceId: Iterable[Int], active: Boolean): Unit

}