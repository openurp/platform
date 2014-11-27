package org.openurp.app

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.resource.service.DbServiceImpl
import org.openurp.resource.action.DbAction
import org.openurp.app.action.AppAction
import org.openurp.app.action.AppAction
import org.openurp.resource.action.DbAction
import org.openurp.resource.service.DbServiceImpl
import org.openurp.app.func.FuncResource
import org.openurp.app.action.FuncResourceAction

class DefaultWebModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[AppAction], classOf[FuncResourceAction])

  }
}