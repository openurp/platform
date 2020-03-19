package org.openurp.platform.admin.action.user

import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.user.model.PasswordConfig

class PasswordConfigAction extends RestfulAction[PasswordConfig] {

  override def index(): View = {
    put("passwordConfigs", entityDao.search(getQueryBuilder))
    forward()
  }

  override protected def saveAndRedirect(entity: PasswordConfig): View = {
    saveOrUpdate(entity)
    redirect("index", "info.save.success")
  }
}
