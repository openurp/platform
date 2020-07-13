package org.openurp.platform.config.service

import org.openurp.platform.config.model.{Domain, Org}

trait DomainService {
  def getDomain: Domain
  def getOrg: Org
}
