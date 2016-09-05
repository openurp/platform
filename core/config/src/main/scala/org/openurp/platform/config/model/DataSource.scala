package org.openurp.platform.config.model

import org.beangle.data.model.{ IntId, Named, Remark }

class DataSource extends IntId with Named with Remark {
  var app: App = _
  var db: Db = _
  var username: String = _
  var password: String = _
  /**
   * 最大活动连接数
   */
  var maxActive: Int = _
}