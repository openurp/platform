package org.openurp.platform.security.model

import java.security.Principal

import org.beangle.security.authz.{ Permission, Resource, Scopes }
import org.openurp.platform.config.model.App
import org.openurp.platform.user.model.Role
import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.Named
import org.beangle.data.model.IntId
import org.beangle.data.model.pojo.Remark
import org.beangle.data.model.pojo.TemporalAt
import org.beangle.data.model.pojo.Enabled
import java.time.ZonedDateTime
import java.time.Instant

class FuncResource extends IntId with Named with Enabled with Resource with Remark {
  var app: App = _
  var scope = Scopes.Public
  var title: String = _
  var actions: Option[String] = None

  def description: String = {
    name + " " + title
  }
}

class FuncPermission extends LongId with Permission with TemporalAt with Remark {
  var role: Role = _
  var resource: FuncResource = _
  var actions: Option[String] = None
  var restrictions: Option[String] = None

  def this(role: Role, resource: FuncResource) {
    this();
    this.role = role
    this.resource = resource
    this.beginAt = Instant.now
  }

  def principal: Principal = role
}

class AppPermission extends IntId with Permission with TemporalAt {
  var app: App = _
  var resource: FuncResource = _
  var actions: Option[String] = None
  var restrictions: Option[String] = None
  def principal = app
}
