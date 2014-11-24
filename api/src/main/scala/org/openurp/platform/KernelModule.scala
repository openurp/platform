package org.openurp.platform

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.platform.app.service.impl.RemoteAppServiceImpl
import org.openurp.platform.security.service.impl.RemoteUserAuthorizer

class KernelModule extends AbstractBindModule {

  protected override def binding(): Unit = {
    bind(classOf[RemoteAppServiceImpl])
    bind(classOf[RemoteUserAuthorizer])
  }
}