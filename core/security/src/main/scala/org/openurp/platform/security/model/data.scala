package org.openurp.platform.security.model

import java.security.Principal
import org.beangle.data.model.{ IntId, LongId, Named, TemporalAt }
import org.beangle.security.authz.{ Permission, Resource, Scopes }
import org.openurp.platform.config.model.App
import org.openurp.platform.user.model.Role
import org.openurp.platform.config.model.Domain

class DataPermission extends LongId with TemporalAt with Permission {
  var domain: Domain = _
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
  var domain: Domain = _
  var scope = Scopes.Public
  var typeName: String = _
  var title: String = _
  var actions: String = _
  var remark: String = _
  def enabled = true
}
