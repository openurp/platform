package org.openurp.platform.portal.web.action

import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.webmvc.api.action.ActionSupport
import org.openurp.platform.api.app.AppConfig
import org.openurp.platform.api.security.Securities
import org.openurp.platform.kernel.model.App
import org.openurp.platform.security.model.User

/**
 * @author chaostone
 */
class AppAction(entityDao: EntityDao) extends ActionSupport {

  def index(): String = {
    val query = OqlBuilder.from(classOf[User], "user")
    query.where("user.code=:code", Securities.user)
    val user = entityDao.search(query).head
    val apps = user.members.map(m => m.role.app).toSet.toBuffer
    val app = entityDao.findBy(classOf[App], "name", List(AppConfig.name)).head
    put("apps", apps -= app)
    forward()
  }
}