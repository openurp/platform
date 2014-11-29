package org.openurp.platform.security.app

import org.beangle.commons.inject.bind.AbstractBindModule

class DefaultModule extends AbstractBindModule {
  
  protected override def binding() {
    bind("web.Interceptor.appsecurity", classOf[SecurityInterceptor])
  }
}