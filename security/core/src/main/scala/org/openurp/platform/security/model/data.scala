package org.openurp.platform.security.model

import java.security.Principal
import org.beangle.data.model.{ Enabled, IntId, LongId, Named, TemporalAt }
import org.beangle.security.blueprint.{ DataPermission, DataResource }
import org.beangle.security.blueprint.Role
import org.openurp.platform.kernel.model.App
import org.beangle.security.blueprint.FuncResource

class UrpDataResource extends IntId with Named with DataResource {
  var title: String = _
  var actions: String = _
  var remark: String = _
}

class UrpDataPermission extends LongId with TemporalAt with DataPermission {
  var app: App = _
  var resource: DataResource = _
  var funcResource: FuncResource = _
  var attrs: String = _
  var filters: String = _
  var actions: String = _
  var restrictions: String = _
  var remark: String = _
  var role: Role = _

  def principal: Principal = role
}