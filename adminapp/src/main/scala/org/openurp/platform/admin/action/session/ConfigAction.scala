package org.openurp.platform.admin.action.session

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.session.model.SessionConfig
import org.openurp.platform.user.model.UserCategory

class ConfigAction extends RestfulAction[SessionConfig] {

  protected override def editSetting(resource: SessionConfig): Unit = {
    put("categories", entityDao.getAll(classOf[UserCategory]))
  }

}
