package org.openurp.platform.security.model

import java.security.Principal

import org.beangle.data.model.{ LongId, TemporalAt }
import org.beangle.security.blueprint.{ FuncPermission, FuncResource, Role }

class UrpFuncPermission extends LongId with TemporalAt with FuncPermission {
  var role: Role = _
  var resource: FuncResource = _
  var actions: String = _
  var restrictions: String = _
  var remark: String = _

  def principal: Principal = role
}
