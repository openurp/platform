package org.openurp.platform.ws.security

import scala.collection.mutable.Buffer
import org.beangle.commons.collection.Properties
import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.webmvc.api.action.{ ActionSupport, EntitySupport }
import org.beangle.webmvc.api.annotation.{ param, response }
import org.openurp.platform.config.model.App
import org.openurp.platform.user.model.User
import org.openurp.platform.user.service.UserService
import org.openurp.platform.security.model.FuncPermission
import org.openurp.platform.user.model.Root
import org.beangle.webmvc.api.annotation.mapping
import org.beangle.commons.collection.Collections

/**
 * @author chaostone
 */
class RootWS(userService: UserService, entityDao: EntityDao) extends ActionSupport with EntitySupport[User] {

  @response
  def index(@param("app") app: String): Seq[String] = {
    val query = OqlBuilder.from[String](classOf[Root].getName, "r")
    query.where("r.app.name = :appName", app).select("r.user.code")
    entityDao.search(query)
  }
}
