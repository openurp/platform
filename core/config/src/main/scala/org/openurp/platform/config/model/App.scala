package org.openurp.platform.config.model

import org.beangle.commons.collection.Collections
import org.beangle.data.model.{ IntId, Named }
import java.security.Principal

class App extends IntId with Named with Principal {
  var secret: String = _
  var title: String = _
  var datasources = Collections.newBuffer[DataSource]
  var remark: String = _
  var appType: String = _
  var url: String = _
  var logoUrl: String = _
  def getName = name
  
}