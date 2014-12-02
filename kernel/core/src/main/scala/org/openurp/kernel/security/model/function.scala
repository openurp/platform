package org.openurp.kernel.security.model

import java.security.Principal
import org.beangle.data.model.bean.{ IntIdBean, TemporalAtBean }
import org.beangle.security.blueprint.{ FuncPermission, Role }
import org.openurp.kernel.app.func.FuncResource

class FuncPermissionBean extends IntIdBean with TemporalAtBean with FuncPermission {
  var role: Role = _
  var resource: FuncResource = _
  var actions: String = _
  var restrictions: String = _
  var remark: String = _

  def principal: Principal = role
}
