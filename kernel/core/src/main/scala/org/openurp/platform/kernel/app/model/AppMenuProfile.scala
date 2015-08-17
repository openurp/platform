package org.openurp.platform.kernel.app.model

import scala.collection.mutable
import org.beangle.data.model.{ Enabled, Hierarchical, IntId, Named }
import org.beangle.security.blueprint.{ FuncResource, Menu, MenuProfile }
import org.beangle.commons.collection.Collections

class AppMenuProfile extends IntId with Named with Enabled with MenuProfile {
  var app: App = _
  var menus = Collections.newBuffer[Menu]
}