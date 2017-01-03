package org.openurp.platform.ws.user

import org.beangle.commons.collection.Properties
import org.beangle.commons.dao.{ EntityDao, OqlBuilder }
import org.beangle.webmvc.api.action.{ ActionSupport, EntitySupport }
import org.beangle.webmvc.api.annotation.{ mapping, param, response }
import org.openurp.platform.user.model.{ RoleMember, User }
import org.openurp.platform.user.service.UserService
import org.openurp.platform.user.model.Dimension

/**
 * @author chaostone
 */
class DimensionWS(entityDao: EntityDao) extends ActionSupport {

  @response
  @mapping("{name}")
  def index(@param("name") name: String): Properties = {
    val dimensions = entityDao.findBy(classOf[Dimension], "name", List(name))
    if (dimensions.isEmpty) new Properties()
    else {
      val dimension = dimensions(0)
      new Properties(dimension, "id", "title", "source", "multiple", "required", "typeName", "keyName", "properties")
    }
  }
}
