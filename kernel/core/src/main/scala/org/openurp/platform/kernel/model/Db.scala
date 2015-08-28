package org.openurp.platform.kernel.model

import org.beangle.data.model.{ IntId, Named }

class Db extends IntId with Named {
  var url: String = _
  var driverClassName: String = _
  var remark: String = _
}
