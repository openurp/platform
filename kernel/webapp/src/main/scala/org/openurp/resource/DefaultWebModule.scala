package org.openurp.resource

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.resource.service.DbServiceImpl
import org.openurp.resource.action.DbAction
import org.openurp.app.action.AppAction
import org.openurp.app.action.AppAction
import org.openurp.resource.action.DbAction
import org.openurp.resource.service.DbServiceImpl

class DefaultWebModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[DbAction])
    bind(classOf[DbServiceImpl])
    
  }
}