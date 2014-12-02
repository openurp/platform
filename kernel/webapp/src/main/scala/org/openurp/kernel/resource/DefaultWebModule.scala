package org.openurp.kernel.resource

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.kernel.resource.action.DbAction
import org.openurp.kernel.resource.service.DbServiceImpl

class DefaultWebModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[DbAction])
    bind(classOf[DbServiceImpl])

  }
}