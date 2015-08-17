package org.openurp.platform.kernel.app.model

import scala.collection.mutable
import org.beangle.data.model.{ Enabled, Hierarchical, IntId, Named }
import org.beangle.security.blueprint.{ FuncResource, Menu, MenuProfile }
import org.beangle.commons.collection.Collections

class AppMenu extends IntId with Named with Enabled with Hierarchical[Menu] with Menu {
  var profile: MenuProfile = _
  var title: String = _
  var entry: FuncResource = _
  var params: String = _
  var remark: String = _
  var resources: mutable.Set[FuncResource] = new mutable.HashSet[FuncResource]
}