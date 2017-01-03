package org.openurp.platform.web

import org.beangle.commons.cdi.bind.AbstractBindModule
import org.openurp.platform.web.action.config.{ AppAction, DbAction }
import org.openurp.platform.web.action.config.AppAction
import org.openurp.platform.web.action.config.DbAction

class ConfigModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[AppAction])
    bind(classOf[DbAction])
  }
}