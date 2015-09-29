package org.openurp.platform.kernel.action

import org.beangle.commons.inject.bind.AbstractBindModule

class DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[AppAction])
    bind(classOf[IndexAction])
    bind(classOf[DbAction])
  }
}