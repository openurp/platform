package org.openurp.platform.security.model

import java.security.Principal

import org.beangle.data.model.{ Enabled, IntId, LongId, Named, TemporalAt }
import org.beangle.security.authz.{ Permission, Resource, Scopes }
import org.openurp.platform.kernel.model.App

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

  def principal: Principal = role
}

class AppPermission extends IntId with Permission with TemporalAt {
  var app: App = _
  var resource: FuncResource = _
  var actions: String = _
  var restrictions: String = _
  def principal = app
}