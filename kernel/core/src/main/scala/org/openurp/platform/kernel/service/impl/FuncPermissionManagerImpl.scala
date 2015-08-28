package org.openurp.platform.kernel.service.impl

import org.beangle.data.model.dao.EntityDao
import org.openurp.platform.kernel.service.AppFuncPermissionManager

class AppFuncPermissionManagerImpl(val entityDao: EntityDao) extends AppFuncPermissionManager {

  def activate(resourceId: Array[Integer], active: Boolean): Unit = {

  }

}