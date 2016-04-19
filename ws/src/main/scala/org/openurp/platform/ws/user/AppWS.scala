package org.openurp.platform.ws.user

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
import org.openurp.platform.security.model.FuncPermission
import org.openurp.platform.user.model.Root

/**
 * @author chaostone
 */
class AppWS(userService: UserService, entityDao: EntityDao) extends ActionSupport with EntitySupport[User] {

  @response
  @mapping("{userCode}")
  def index(@param("userCode") userCode: String): Seq[Properties] = {
    val query = OqlBuilder.from(classOf[User], "user")
    query.where("user.code=:code", userCode)
    val users = entityDao.search(query)
    if (users.isEmpty) return Seq.empty
    val user = users.head
    val fpApps = entityDao.search(OqlBuilder.from[App](classOf[FuncPermission].getName, "fp").join("fp.role.members", "m")
      .where("m.user=:user and m.member=true", user)
      .where("fp.resource.app.enabled=true")
      .select("distinct fp.resource.app"))

    val apps = Collections.newSet[App]
    apps ++= fpApps

    val rootsQuery = OqlBuilder.from(classOf[Root], "root").where("root.user=:user and root.app.enabled=true", user)
    val roots = entityDao.search(rootsQuery)
    apps ++= (roots.map(a => a.app))
    val appBuffer = apps.toBuffer.sorted
    appBuffer.map(app => new Properties(app, "id", "name", "title", "url", "logoUrl"))
  }
}
