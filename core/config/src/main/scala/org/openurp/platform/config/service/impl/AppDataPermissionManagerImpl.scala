package org.openurp.platform.config.service.impl

import org.beangle.commons.dao.EntityDao
import org.openurp.platform.config.service.AppDataPermissionManager

class AppDataPermissionManagerImpl(val entityDao: EntityDao) extends AppDataPermissionManager {

  def activate(resourceId: Iterable[Int], active: Boolean): Unit = {

  }

}