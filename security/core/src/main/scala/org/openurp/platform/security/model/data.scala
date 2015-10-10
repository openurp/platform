package org.openurp.platform.security.model

import java.security.Principal
import org.beangle.data.model.{ Enabled, IntId, LongId, Named, TemporalAt }
import org.openurp.platform.kernel.model.App
import org.beangle.security.authz.Permission
import org.beangle.security.authz.Scopes
import org.beangle.security.authz.Resource


class DataPermission extends LongId with TemporalAt with Permission {
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

class DataResource extends IntId with Named with Resource {
  var scope = Scopes.Public
  var typeName: String = _
  var title: String = _
  var actions: String = _
  var remark: String = _
  def enabled = true
}

