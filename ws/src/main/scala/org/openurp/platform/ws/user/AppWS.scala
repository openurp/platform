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
    userService.get(userCode) match {
      case Some(user) =>
        val fpAppQuery = OqlBuilder.from[App](classOf[FuncPermission].getName, "fp").join("fp.role.members", "m")
          .where("m.user=:user and m.member=true", user)
          .where("fp.resource.app.enabled=true")
          .where("fp.resource.app.appType='web-app'")
          .select("distinct fp.resource.app").cacheable()

        val fpApps = entityDao.search(fpAppQuery)

        val apps = Collections.newSet[App]
        apps ++= fpApps

        val rootsQuery = OqlBuilder.from(classOf[Root], "root")
          .where("root.user=:user and root.app.enabled=true and root.app.appType='web-app'", user)
          .cacheable()
        val roots = entityDao.search(rootsQuery)
        apps ++= (roots.map(a => a.app))
        val domain = get("domain")
        domain foreach { d =>
          apps --= apps.filter { a => a.domain == null || a.domain.name != d }
        }
        val appBuffer = apps.toBuffer.sorted
        appBuffer.map(app => new Properties(app, "id", "name", "title", "url", "logoUrl"))
      case None => Seq.empty
    }
  }
}
