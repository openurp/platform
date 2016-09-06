package org.openurp.platform.config.model

import org.beangle.data.model.{ IntId, Named, Remark }

class Db extends IntId with Named with Remark{
  var url: Option[String] = None
  var driver: String = _
  var serverName: String = _
  var databaseName: String = _
  var portNumber: Int = _
}
