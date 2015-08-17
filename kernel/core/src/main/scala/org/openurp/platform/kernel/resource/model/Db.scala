package org.openurp.platform.kernel.resource.model

import org.beangle.data.model.{ IntId, Named }
import org.openurp.platform.kernel.app.model.App

class Db extends IntId with Named {
  var url: String = _
  var driverClassName: String = _
  var remark: String = _
}
