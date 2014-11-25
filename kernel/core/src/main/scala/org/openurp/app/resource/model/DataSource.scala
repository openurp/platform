package org.openurp.app.resource.model

import org.beangle.data.model.bean.{ IntIdBean, NamedBean }
import org.openurp.app.App

class DataSourceCfgBean extends IntIdBean with NamedBean {
  var url: String = _
  var driverClassName: String = _
  var remark: String = _
}

class DataSourceBean extends IntIdBean with NamedBean {
  var app: App = _
  var config: DataSourceCfgBean = _
  var username: String = _
  var password: String = _
  /**
   * 最大活动连接数
   */
  var maxActive: Int = _
  var remark: String = _
}