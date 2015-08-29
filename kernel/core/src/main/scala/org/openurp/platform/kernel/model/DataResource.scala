package org.openurp.platform.kernel.model

import org.beangle.data.model.{ IntId, Named }
import org.beangle.security.authz.{ Resource, Scopes }

class DataResource extends IntId with Named with Resource {
  var scope = Scopes.Public
  var typeName: String = _
  var title: String = _
  var actions: String = _
  var remark: String = _
  def enabled = true
}

