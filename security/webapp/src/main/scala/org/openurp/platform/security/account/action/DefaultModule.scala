package org.openurp.platform.security.account.action

import org.beangle.commons.inject.bind.AbstractBindModule

class DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[AccountAction], classOf[IndexAction])
  }
}