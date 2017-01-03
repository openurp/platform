package org.openurp.platform.security.model

import scala.collection.mutable
import org.beangle.commons.collection.Collections
import org.beangle.commons.model.{ Enabled, Hierarchical, IntId, Named }
import org.openurp.platform.config.model.App
import org.beangle.commons.lang.Numbers
import org.beangle.commons.lang.Strings
import org.openurp.platform.user.model.Role
import org.beangle.commons.model.Remark

class Menu extends IntId with Named with Enabled with Hierarchical[Menu] with Remark {
  var app: App = _
  var title: String = _
  var entry: Option[FuncResource] = None
  var params: Option[String] = None
  var resources: mutable.Set[FuncResource] = new mutable.HashSet[FuncResource]

  def description: String = {
    indexno + " " + title
  }

  override def compare(m: Menu): Int = {
    indexno.compareTo(m.indexno)
  }
}
