package org.openurp.platform.security.model

import scala.collection.mutable
import org.beangle.commons.collection.Collections
import org.beangle.data.model.{ Enabled, Hierarchical, IntId, Named }
import org.openurp.platform.kernel.model.App
import org.beangle.commons.lang.Numbers
import org.beangle.commons.lang.Strings

class MenuProfile extends IntId with Named with Enabled {
  var app: App = _
  var menus = Collections.newBuffer[Menu]
  var role: Role = _
}

class Menu extends IntId with Named with Enabled with Hierarchical[Menu] {
  var profile: MenuProfile = _
  var title: String = _
  var entry: FuncResource = _
  var params: String = _
  var remark: String = _
  var resources: mutable.Set[FuncResource] = new mutable.HashSet[FuncResource]

  def description: String = {
    indexno + " " + title
  }

}

