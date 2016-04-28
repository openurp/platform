package org.openurp.platform.security.model

import scala.collection.mutable
import org.beangle.commons.collection.Collections
import org.beangle.data.model.{ Enabled, Hierarchical, IntId, Named }
import org.openurp.platform.config.model.App
import org.beangle.commons.lang.Numbers
import org.beangle.commons.lang.Strings
import org.openurp.platform.user.model.Role

class Menu extends IntId with Named with Enabled with Hierarchical[Menu] with Ordered[Menu] {
  var app: App = _
  var title: String = _
  var entry: FuncResource = _
  var params: String = _
  var remark: String = _
  var resources: mutable.Set[FuncResource] = new mutable.HashSet[FuncResource]

  def description: String = {
    indexno + " " + title
  }

  override def compare(m: Menu): Int = {
    indexno.compareTo(m.indexno)
  }
}
