package org.openurp.platform.api.security

import org.beangle.security.context.SecurityContext

object Securities {

  def user: String = {
    SecurityContext.session.principal.getName()
  }

}