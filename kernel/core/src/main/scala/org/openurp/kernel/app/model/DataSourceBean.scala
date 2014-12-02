package org.openurp.kernel.app.model

import org.beangle.data.model.bean.NamedBean
import org.beangle.data.model.bean.IntIdBean
import org.openurp.kernel.resource.model.DbBean
import org.openurp.kernel.app.App

class DataSourceBean extends IntIdBean with NamedBean {
  var app: App = _
  var db: DbBean = _
  var username: String = _
  var password: String = _
  /**
   * 最大活动连接数
   */
  var maxActive: Int = _
  var remark: String = _
}