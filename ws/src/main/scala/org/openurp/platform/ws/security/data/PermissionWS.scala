package org.openurp.platform.ws.security.data

import org.beangle.commons.collection.{ Collections, Properties }
import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.{ mapping, param, response }
import org.openurp.platform.config.model.App
import org.openurp.platform.security.model.DataPermission
import org.openurp.platform.user.model.User

/**
 * @author chaostone
 */
class PermissionWS(entityDao: EntityDao) extends ActionSupport {

  @response
  @mapping("user/{userCode}")
  def index(@param("app") appName: String, @param("userCode") userCode: String, @param("data") dataName: String): Any = {
    val userQuery = OqlBuilder.from(classOf[User], "u").where("u.code =:userCode", userCode)
    val users = entityDao.search(userQuery)
    val apps = entityDao.findBy(classOf[App], "name", List(appName));
    if (users.isEmpty || apps.isEmpty) return List.empty
    else {
      val u = users.head
      val app = apps.head

      val roleSet = u.roles.filter(r => r.member).map(r => r.role).toSet
      val permissionQuery = OqlBuilder.from(classOf[DataPermission], "dp")
      permissionQuery.where("dp.domain=:domain and dp.resource.name=:dataName", app.domain, dataName)
        .cacheable(true)
      val permissions = entityDao.search(permissionQuery)
      val favorates = Collections.newBuffer[DataPermission]
      val mostFavorates = permissions find (p => null != p.app && null != p.role && roleSet.contains(p.role))
      val p = mostFavorates match {
        case Some(p) => p
        case None =>
          permissions find (x => null != x.app && null == x.role) match {
            case Some(p) => p
            case None =>
              permissions find (x => null == x.app && null != x.role) match {
                case Some(p) => p
                case None => {
                  val pp = permissions find (x => null == x.app && null == x.role)
                  pp.orNull
                }
              }
          }
      }
      val props = new Properties()
      if (p != null) {
        props.put("filters", p.filters)
        //props.put("description", p.description)
      }
      props
    }
  }
}
