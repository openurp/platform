package org.openurp.platform.kernel.app.action

import org.beangle.commons.inject.bind.AbstractBindModule

class DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[AppAction], classOf[FuncResourceAction])

  }
}