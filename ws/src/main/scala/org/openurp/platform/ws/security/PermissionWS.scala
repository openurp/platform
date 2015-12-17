package org.openurp.platform.ws.security

import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.param
import org.beangle.data.dao.EntityDao

/**
 * @author chaostone
 */
class PermissionWS(entityDao: EntityDao) extends ActionSupport {
  
  def role(@param("app") app: String, id: Int): Any = {
    
  }
}