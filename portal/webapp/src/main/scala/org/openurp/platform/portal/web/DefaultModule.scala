package org.openurp.platform.portal.web

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.platform.portal.web.action.AppAction
import org.openurp.platform.portal.web.action.IndexAction
import org.beangle.commons.inject.PropertySource

class DefaultModule extends AbstractBindModule    {

  override def binding() {
    bind(classOf[AppAction], classOf[IndexAction])
  }

}

class DefaultServiceModule extends AbstractBindModule with PropertySource {

  override def binding() {
  }

  override def properties: collection.Map[String, String] = {
    Map("security.cas.server" -> "http://localhost:9080/cas")
  }
}
