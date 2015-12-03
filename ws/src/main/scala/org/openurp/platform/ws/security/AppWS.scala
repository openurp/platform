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

/**
 * @author chaostone
 */
class AppWS(userService: UserService, entityDao: EntityDao) extends ActionSupport with EntitySupport[User] {

  @response
  def index(@param("name") username: String): Seq[Properties] = {
    val query = OqlBuilder.from(classOf[User], "user")
    query.where("user.code=:code", username)
    val user = entityDao.search(query).head
    val fpquery = entityDao.search(OqlBuilder.from(classOf[FuncPermission], "fp").join("fp.role.members", "m")
      .where("m.user=:user", user).select("distinct fp.resource.app"))

    val apps = fpquery.toBuffer.asInstanceOf[Buffer[App]]

    val rootsQuery = OqlBuilder.from(classOf[Root], "root").where("root.user=:user", user)
    val roots = entityDao.search(rootsQuery)
    apps ++= (roots.map(a => a.app))
    apps.map(app => new Properties(app, "id", "name", "url", "logoUrl"))
  }
}