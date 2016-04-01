package org.openurp.platform.ws.security

import org.beangle.commons.collection.Properties
import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.{ mapping, param, response }
import org.openurp.platform.security.model.{ FuncPermission, FuncResource }

/**
 * @author chaostone
 */
class FuncPermissionWS(entityDao: EntityDao) extends ActionSupport {

  @response
  @mapping("role/{roleId}")
  def role(@param("app") app: String, @param("roleId") roleId: Int): Any = {
    val roleQuery = OqlBuilder.from[FuncResource](classOf[FuncPermission].getName, "fp")
      .where("fp.resource.app.name = :appName", app).where("fp.role.id = :roleId", roleId)
      .where("fp.endAt is null  or fp.endAt < :now)", new java.util.Date).select("fp.resource")
    val resources = entityDao.search(roleQuery)
    resources.map { r => new Properties(r, "id", "name", "title", "scope") }
  }
}
