package org.openurp.kernel.app.func.model

import org.beangle.data.model.bean.{ EnabledBean, IntIdBean, NamedBean }
import org.beangle.security.blueprint.Scopes
import org.openurp.kernel.app.App
import org.openurp.kernel.app.func.FuncResource

class FuncResourceBean extends IntIdBean with NamedBean with EnabledBean with FuncResource {
  var app: App = _
  var scope = Scopes.Public
  var title: String = _
  var actions: String = _
  var remark: String = _
}
