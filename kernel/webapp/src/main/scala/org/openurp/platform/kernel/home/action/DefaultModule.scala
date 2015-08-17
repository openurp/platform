package org.openurp.platform.kernel.home.action

import org.beangle.commons.inject.bind.AbstractBindModule

object DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[IndexAction])
  }

}