package org.openurp.platform.kernel.app.model

import org.beangle.data.model.{ Enabled, IntId, Named }
import org.beangle.security.blueprint.{ Scopes, FuncResource }

class AppFuncResource extends IntId with Named with Enabled with FuncResource {
  var app: App = _
  var scope = Scopes.Public
  var title: String = _
  var actions: String = _
  var remark: String = _
}
