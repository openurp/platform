package org.openurp.platform.kernel.app.model

import scala.collection.mutable
import org.beangle.data.model.{ IntId, Named }
import org.beangle.security.blueprint.{ FuncPermission, FuncResource }
import org.beangle.commons.collection.Collections

class App extends IntId with Named {
  var secret: String = _
  var title: String = _
  var datasources = Collections.newBuffer[DataSource]
  var funcResources: mutable.Set[FuncResource] = new mutable.HashSet[FuncResource]
  var funcPermissions = Collections.newBuffer[AppFuncPermission]
  var remark: String = _
  var appType: String = _
  var url: String = _
}