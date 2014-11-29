package org.openurp.platform.ws

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.platform.ws.security.AppSecurityInterceptor

class DefaultModule extends AbstractBindModule {
  
  protected override def binding() {
    bind("web.Interceptor.appsecurity", classOf[AppSecurityInterceptor])
  }
}