package org.openurp.kernel.app.ws

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.kernel.app.ws.func.{ MenuProfileWS, MenuWS, ResourceWS }
import org.openurp.kernel.app.ws.resource.DatasourceWS
import org.openurp.kernel.app.ws.security.{ LoginWS, ValidateWS }

class DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[DatasourceWS])
    bind(classOf[MenuWS], classOf[MenuProfileWS], classOf[ResourceWS])
    bind(classOf[LoginWS], classOf[ValidateWS])
  }
}