package org.openurp.platform.kernel.model

import org.beangle.data.model.IntId
import org.beangle.security.blueprint.FuncResource

class AppFuncPermission extends IntId  {
  var app: App = _
  var resource: FuncResource = _
  var actions: String = _
  var restrictions: String = _
}