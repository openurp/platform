package org.openurp.platform.ws.user

import org.beangle.commons.collection.Properties
import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.{ mapping, param, response }
import org.openurp.platform.security.model.{ FuncPermission, FuncResource }
import org.openurp.platform.user.model.User
import org.openurp.platform.user.model.UserProfile
import scala.collection.mutable.ArrayBuffer
import org.openurp.platform.user.model.Profile
import org.beangle.commons.collection.Collections

/**
 * @author chaostone
 */
class ProfileWS(entityDao: EntityDao) extends ActionSupport {

  @response
  @mapping("{userCode}")
  def index(@param("app") app: String, @param("userCode") userCode: String): Any = {

    val userQuery = OqlBuilder.from(classOf[User], "u").where("u.code =:userCode", userCode);
    val users = entityDao.search(userQuery);
    if (users.isEmpty) return List.empty
    else {
      val u = users.head
      val userProfileQuery = OqlBuilder.from(classOf[UserProfile], "up")
        .where("up.user =:user", u)
        .where("up.app.name=:appName", app)

      val appProfiles = entityDao.search(userProfileQuery);
      val profiles = if (u.properties.isEmpty) appProfiles else List(u) ++ appProfiles

      profiles map { profile =>
        val p = new Properties()
        val properties = Collections.newBuffer[Properties]
        p.put("properties", properties)
        profile.properties foreach {
          case (d, v) =>
            val entry = new Properties()
            val dimension = new Properties()
            dimension.put("id", d.id)
            dimension.put("name", d.name)
            dimension.put("title", d.title)
            entry.put("dimension", dimension)
            entry.put("value", v)
            properties += entry
        }
        p
      }
    }
  }
}
