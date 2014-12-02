package org.openurp.app.func.model

import org.openurp.app.func.AppFuncPermission
import org.openurp.app.App
import org.beangle.data.model.bean.IntIdBean
import org.openurp.app.func.FuncResource

class AppFuncPermissionBean extends IntIdBean with AppFuncPermission {
  var app: App = _
  var resource: FuncResource = _
  var actions: String = _
  var restrictions: String = _
}