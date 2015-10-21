package org.openurp.platform.portal.web

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.platform.portal.web.action.AppAction
import org.openurp.platform.portal.web.action.IndexAction
import org.beangle.commons.inject.PropertySource
import org.openurp.platform.api.Urp

class DefaultModule extends AbstractBindModule {

  override def binding() {
    bind(classOf[AppAction], classOf[IndexAction])
  }

}

class DefaultServiceModule extends AbstractBindModule with PropertySource {

  override def binding() {
  }

  override def properties: collection.Map[String, String] = {
    val casUrl = Urp.properties.get("security.cas.server").getOrElse(Urp.platformBase + "/cas")
    Map("security.cas.server" -> casUrl)
  }
}
