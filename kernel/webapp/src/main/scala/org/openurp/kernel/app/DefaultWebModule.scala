package org.openurp.kernel.app

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.kernel.app.action.{ AppAction, FuncResourceAction }

class DefaultWebModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[AppAction], classOf[FuncResourceAction])

  }
}