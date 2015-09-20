package org.openurp.platform.security.model

import java.security.Principal
import org.beangle.data.model.{ Enabled, IntId, LongId, Named, TemporalAt }
import org.openurp.platform.kernel.model.App
import org.openurp.platform.kernel.model.DataResource
import org.beangle.security.authz.Permission


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