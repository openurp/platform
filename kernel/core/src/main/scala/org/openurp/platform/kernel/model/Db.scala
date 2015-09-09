package org.openurp.platform.kernel.model

import org.beangle.data.model.{ IntId, Named }

class Db extends IntId with Named {
  var url: String = _
  var driver: String = _
  var serverName: String = _
  var databaseName: String = _
  var portNumber: Int = _
  var remark: String = _
}
