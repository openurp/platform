package org.openurp.platform.web

import org.beangle.cdi.bind.BindModule
import org.openurp.platform.web.action.IndexAction

class IndexModule extends BindModule {
  override def binding() {
    bind(classOf[IndexAction])
  }
}