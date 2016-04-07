package org.openurp.platform.security.model

import java.security.Principal
import org.beangle.data.model.{ Enabled, IntId, LongId, Named, TemporalAt }
import org.beangle.security.authz.{ Permission, Resource, Scopes }
import org.openurp.platform.config.model.App
import org.openurp.platform.user.model.Role

class FuncResource extends IntId with Named with Enabled with Resource {
  var app: App = _
  var scope = Scopes.Public
  var title: String = _
  var actions: String = _
  var remark: String = _

  def description: String = {
    name + " " + title
  }
}

class FuncPermission extends LongId with TemporalAt with Permission {
  var role: Role = _
  var resource: FuncResource = _
  var actions: String = _
  var restrictions: String = _
  var remark: String = _

  def this(role: Role, resource: FuncResource) {
    this();
    this.role = role
    this.resource = resource
    this.beginAt = new java.util.Date
  }

  def principal: Principal = role
}

class AppPermission extends IntId with Permission with TemporalAt {
  var app: App = _
  var resource: FuncResource = _
  var actions: String = _
  var restrictions: String = _
  def principal = app
}
