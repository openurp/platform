package org.openurp.platform.security.model

import scala.collection.mutable

import org.beangle.commons.collection.Collections
import org.beangle.data.model.{ Enabled, Hierarchical, IntId, Named }
import org.openurp.platform.kernel.model.App

class MenuProfile extends IntId with Named with Enabled {
  var app: App = _
  var menus = Collections.newBuffer[Menu]
  var role: Role = _
}

class Menu extends IntId with Named with Enabled with Hierarchical[Menu] with Ordered[Menu] {
  var profile: MenuProfile = _
  var title: String = _
  var entry: FuncResource = _
  var params: String = _
  var remark: String = _
  var resources: mutable.Set[FuncResource] = new mutable.HashSet[FuncResource]

  override def compare(other: Menu): Int = {
    indexno.compareTo(other.indexno)
  }
}

