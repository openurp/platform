package org.openurp.platform.security.model

import scala.collection.mutable

import org.beangle.data.model.IntId
import org.beangle.data.model.pojo.{ Enabled, Hierarchical, Named, Remark }
import org.openurp.platform.config.model.App

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
