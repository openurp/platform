package org.openurp.platform.kernel.app.ws

import org.beangle.commons.inject.bind.AbstractBindModule

class DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[DatasourceWS])
    bind(classOf[MenuWS], classOf[MenuProfileWS], classOf[FuncResourceWS])
    bind(classOf[LoginWS], classOf[ValidateWS])
  }
}