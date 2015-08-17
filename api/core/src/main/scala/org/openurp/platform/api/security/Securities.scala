package org.openurp.platform.api.security

import org.beangle.security.context.SecurityContext

object Securities {
  
  def loginUserId: java.lang.Long = {
    SecurityContext.session.principal.id.asInstanceOf[java.lang.Long]
    1L
  }
  
    def isAdmin: Boolean = {
    true
  }
  
  
}