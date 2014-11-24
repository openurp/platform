package org.openurp.platform.security.service.impl

import org.beangle.security.authz.Authorizer
import org.beangle.commons.security.Request

class RemoteUserAuthorizer extends Authorizer {

  def isPermitted(principal: Any, request: Request): Boolean = {
    true
  }
}