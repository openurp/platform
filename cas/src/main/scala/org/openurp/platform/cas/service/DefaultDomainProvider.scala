package org.openurp.platform.cas.service

import org.beangle.security.session.jdbc.DomainProvider
import org.openurp.platform.config.service.DomainService

class DefaultDomainProvider extends DomainProvider {

  var domainService: DomainService = _

  override def getDomainId: Int = {
    domainService.getDomain.id
  }

}
