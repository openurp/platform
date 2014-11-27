package org.openurp

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.resource.service.DbServiceImpl
import org.openurp.resource.action.DbAction
import org.openurp.app.action.AppAction

class KernelAppModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[DbAction], classOf[AppAction])
    bind(classOf[DbServiceImpl])
  }
}