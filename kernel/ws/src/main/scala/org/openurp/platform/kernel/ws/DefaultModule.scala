package org.openurp.platform.kernel.ws

import org.beangle.commons.inject.bind.AbstractBindModule

class DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[DatasourceWS])
    bind(classOf[DataResourceWS])
    bind(classOf[LoginWS], classOf[ValidateWS])
  }
}