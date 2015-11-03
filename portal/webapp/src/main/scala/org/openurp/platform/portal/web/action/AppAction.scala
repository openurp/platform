package org.openurp.platform.portal.web.action

import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.webmvc.api.action.ActionSupport
import org.openurp.platform.api.app.UrpApp
import org.openurp.platform.api.security.Securities
import org.openurp.platform.kernel.model.App
import org.openurp.platform.security.model.User
import org.openurp.platform.security.model.Root
import org.openurp.platform.security.model.FuncPermission
import scala.collection.mutable.Buffer

/**
 * @author chaostone
 */
class AppAction(entityDao: EntityDao) extends ActionSupport {

  def index(): String = {
    val query = OqlBuilder.from(classOf[User], "user")
    query.where("user.code=:code", Securities.user)
    val user = entityDao.search(query).head
    val fpquery = entityDao.search(OqlBuilder.from(classOf[FuncPermission], "fp").join("fp.role.members", "m")
      .where("m.user=:user", user).select("distinct fp.resource.app"))

    val apps = fpquery.toBuffer.asInstanceOf[Buffer[App]]

    val rootsQuery = OqlBuilder.from(classOf[Root], "root").where("root.user=:user", user)
    apps ++= entityDao.search(rootsQuery).map(a => a.app)

    val app = entityDao.findBy(classOf[App], "name", List(UrpApp.name)).head
    val finalApps = apps.toSet.toBuffer
    put("apps", finalApps -= app)
    forward()
  }
}