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
import org.openurp.platform.user.service.UserService

/**
 * @author chaostone
 */
class ProfileWS(entityDao: EntityDao) extends ActionSupport {

  var userService: UserService = _

  @response
  @mapping("{userCode}")
  def index(@param("userCode") userCode: String, @param("domain") domain: String): Any = {
    userService.get(userCode) match {
      case Some(user) =>
        val userProfileQuery = OqlBuilder.from(classOf[UserProfile], "up")
          .where("up.user =:user", user)
          .where("up.domain.name=:domainName", domain)
          .cacheable()

        val appProfiles = entityDao.search(userProfileQuery);
        val profiles = if (user.properties.isEmpty) appProfiles else List(user) ++ appProfiles

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
              dimension.put("typeName", d.typeName)
              dimension.put("keyName", d.keyName)
              entry.put("dimension", dimension)
              entry.put("value", v)
              properties += entry
          }
          p
        }
      case None => List.empty
    }
  }
}
