package org.openurp.security.model

import java.security.Principal
import org.beangle.data.model.bean.{ EnabledBean, IntIdBean, NamedBean, TemporalAtBean }
import org.beangle.security.blueprint.{ DataField, DataPermission, DataResource }
import org.beangle.security.blueprint.Role

class DataResourceBean extends IntIdBean with NamedBean with EnabledBean with DataResource {
  var title: String = _
  var actions: String = _
  var remark: String = _
}

class DataFieldBean extends IntIdBean with NamedBean with DataField {
  var title: String = _
  var resource: DataResource = _
  var typeName: String = _
}

class DataPermissionBean extends IntIdBean with TemporalAtBean with DataPermission {
  var resource: DataResource = _
  var attrs: String = _
  var filters: String = _
  var actions: String = _
  var restrictions: String = _
  var remark: String = _
  var role: Role = _

  def principal: Principal = role
}