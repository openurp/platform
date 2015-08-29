package org.openurp.platform.kernel.service.impl

import org.beangle.data.model.dao.EntityDao
import org.openurp.platform.kernel.service.AppDataPermissionManager

class AppDataPermissionManagerImpl(val entityDao: EntityDao) extends AppDataPermissionManager {

  def activate(resourceId: Array[Integer], active: Boolean): Unit = {

  }

}