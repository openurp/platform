package org.openurp.app.ws

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.app.ws.resource.IndexWS
import org.openurp.app.ws.nav.MenuProfileWS
import org.openurp.app.ws.nav.MenuWS

class DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[IndexWS])
     bind(classOf[MenuWS], classOf[MenuProfileWS])
  }
}