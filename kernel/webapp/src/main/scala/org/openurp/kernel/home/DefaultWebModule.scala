package org.openurp.kernel.home

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.kernel.home.action.IndexAction

class DefaultWebModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[IndexAction])
  }

}