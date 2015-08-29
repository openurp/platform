package org.openurp.platform.kernel.model

import org.beangle.data.model.IntId
import org.beangle.security.authz.Permission
import org.beangle.data.model.TemporalAt

class AppDataPermission extends IntId with Permission with TemporalAt {
  var app: App = _
  var resource: DataResource = _
  var actions: String = _
  var restrictions: String = _
  def principal = app
}