package org.openurp.platform.config.model

import java.security.Principal

import org.beangle.commons.collection.Collections
import org.beangle.commons.model.{ Enabled, IntId, Named, Remark }

class App extends IntId with Named with Enabled with Ordered[App] with Principal with Remark {
  var secret: String = _
  var title: String = _
  var datasources = Collections.newBuffer[DataSource]
  var appType: String = _
  var url: String = _
  var logoUrl: Option[String] = None
  var indexno: String = _
  var domain: Domain = _

  def getName = name

  def fullTitle = domain.title + " " + title

  override def compare(m: App): Int = {
    indexno.compareTo(m.indexno)
  }

}
