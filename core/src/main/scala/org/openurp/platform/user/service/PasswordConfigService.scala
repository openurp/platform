package org.openurp.platform.user.service

import org.openurp.platform.user.model.PasswordConfig

trait PasswordConfigService {

  def get(): PasswordConfig
}
