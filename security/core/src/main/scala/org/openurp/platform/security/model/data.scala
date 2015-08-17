package org.openurp.platform.security.model

import java.security.Principal
import org.beangle.data.model.{ Enabled, IntId, LongId, Named, TemporalAt }
import org.beangle.security.blueprint.{ DataField, DataPermission, DataResource }
import org.beangle.security.blueprint.Role

class UrpDataResource extends IntId with Named with Enabled with DataResource {
  var title: String = _
  var actions: String = _
  var remark: String = _
}

class UrpDataField extends IntId with Named with DataField {
  var title: String = _
  var resource: DataResource = _
  var typeName: String = _
}

class UrpDataPermission extends LongId with TemporalAt with DataPermission {
  var resource: DataResource = _
  var attrs: String = _
  var filters: String = _
  var actions: String = _
  var restrictions: String = _
  var remark: String = _
  var role: Role = _

  def principal: Principal = role
}