package org.openurp.platform.ws.security

import org.beangle.commons.collection.Properties
import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.{ mapping, param, response }
import org.openurp.platform.security.model.{ FuncPermission, FuncResource }
import org.openurp.platform.user.model.User
import scala.collection.mutable.ArrayBuffer
import org.openurp.platform.user.model.Profile
import org.beangle.commons.collection.Collections
import org.openurp.platform.user.model.UserProfile

/**
 * @author chaostone
 */
class DataPermissionWS(entityDao: EntityDao) extends ActionSupport {

  @response
  @mapping("user/{userCode}")
  def role(@param("app") app: String, @param("userCode") userCode: String): Any = {
    val userQuery = OqlBuilder.from(classOf[User], "u").where("u.code =:userCode");
    val users = entityDao.search(userQuery);
    if (users.isEmpty) return List.empty
    else {
      val u = users.head
      val userProfileQuery = OqlBuilder.from(classOf[UserProfile], "up")
        .where("up.user =:user", u)
        .where("urp.app.name=:appName", app)

      val appProfiles = entityDao.search(userProfileQuery);
      val profiles = if (u.properties.isEmpty) appProfiles else List(u) ++ appProfiles
      profiles map { profile =>
        val p = new Properties()
        val properties = Collections.newBuffer[Properties]
        p.put("properties", properties)
        profile.properties foreach {
          case (d, v) =>
            val entry = new Properties()
            entry.put("dimension", new Properties(d, "id", "name", "title"))
            entry.put("value", v)
            properties += entry
        }
      }
    }
  }
}
