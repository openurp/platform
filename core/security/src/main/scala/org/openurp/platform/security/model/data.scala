package org.openurp.platform.security.model

import java.security.Principal
import java.time.Instant

import org.beangle.data.model.{ IntId, LongId }
import org.beangle.data.model.pojo.{ Named, Remark }
import org.beangle.security.authz.{ Permission, Resource, Scopes }
import org.openurp.platform.config.model.{ App, Domain }
import org.openurp.platform.user.model.Role

class DataPermission extends LongId  with Permission with Remark {
  var domain: Domain = _
  var app: Option[App] = None
  var resource: DataResource = _
  var description: String = _
  var filters: String = _
  var funcResource: Option[FuncResource] = None
  var attrs: Option[String] = None
  var actions: Option[String] = None
  var restrictions: Option[String] = None
  var role: Option[Role] = None

  var beginAt:Instant=_
  var endAt:Option[Instant]=None
  def principal: Principal = role.orNull
}

class DataResource extends IntId with Named with Resource with Remark {
  var domain: Domain = _
  var scope = Scopes.Public
  var typeName: String = _
  var title: String = _
  var actions: Option[String] = None
  def enabled = true
}
