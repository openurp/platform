package org.openurp.platform.config.model

import java.security.Principal

import org.beangle.commons.collection.Collections
import org.beangle.data.model.{ Enabled, IntId, Named }

class App extends IntId with Named with Enabled with Ordered[App] with Principal {
  var secret: String = _
  var title: String = _
  var datasources = Collections.newBuffer[DataSource]
  var remark: String = _
  var appType: String = _
  var url: String = _
  var logoUrl: String = _
  var indexno: String = _
  var domain: Domain = _

  def getName = name

  def fullTitle = domain.title + " " + title

  override def compare(m: App): Int = {
    indexno.compareTo(m.indexno)
  }

}
