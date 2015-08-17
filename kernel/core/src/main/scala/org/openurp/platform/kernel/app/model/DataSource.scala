package org.openurp.platform.kernel.app.model

import org.beangle.data.model.Named
import org.beangle.data.model.IntId
import org.openurp.platform.kernel.resource.model.Db

class DataSource extends IntId with Named {
  var app: App = _
  var db: Db = _
  var username: String = _
  var password: String = _
  /**
   * 最大活动连接数
   */
  var maxActive: Int = _
  var remark: String = _
}