package org.openurp.platform.security.ws

import org.beangle.commons.inject.bind.AbstractBindModule

class DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[MenuWS], classOf[MenuProfileWS])
    bind(classOf[FuncResourceWS])
  }
}