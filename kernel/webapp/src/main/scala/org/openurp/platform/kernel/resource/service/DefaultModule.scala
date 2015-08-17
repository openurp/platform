package org.openurp.platform.kernel.resource.service

import org.beangle.commons.inject.bind.AbstractBindModule

class DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[DbServiceImpl])

  }
}