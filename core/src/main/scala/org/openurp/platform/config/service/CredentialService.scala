package org.openurp.platform.config.service

import org.openurp.platform.config.model.Credential

trait CredentialService {

  def getAll():Seq[Credential]
}
