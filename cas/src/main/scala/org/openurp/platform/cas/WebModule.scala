package org.openurp.platform.cas

import org.beangle.cdi.bind.BindModule
import org.beangle.ids.cas.web.action.{ LoginAction, LogoutAction, ServiceValidateAction, SessionAction, SessionsAction }

class WebModule extends BindModule {
  override def binding() {
    bind(classOf[LoginAction])
    bind(classOf[ServiceValidateAction])
    bind(classOf[LogoutAction])
    bind(classOf[SessionAction])
    bind(classOf[SessionsAction]).constructor(ref("DataSource.session"))
  }
}