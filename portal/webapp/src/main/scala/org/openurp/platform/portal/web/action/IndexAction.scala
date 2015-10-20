package org.openurp.platform.portal.web.action

import org.beangle.data.dao.EntityDao
import org.beangle.webmvc.api.action.ActionSupport
import org.openurp.platform.api.app.AppConfig
import org.openurp.platform.kernel.model.App
import org.beangle.security.context.SecurityContext
import org.beangle.webmvc.api.view.View
import org.beangle.security.mgt.SecurityManager
import org.beangle.security.realm.cas.CasConfig
/**
 * @author chaostone
 */
class IndexAction(entityDao: EntityDao, securityManager: SecurityManager) extends ActionSupport {

  var config: CasConfig = _

  def index(): String = {
    val app = entityDao.findBy(classOf[App], "name", List(AppConfig.name)).head
    put("app", app)
    forward()
  }

  def logout(): View = {
    securityManager.logout(SecurityContext.session)
    redirect(to(config.casServer + "/logout"), null)
  }

}