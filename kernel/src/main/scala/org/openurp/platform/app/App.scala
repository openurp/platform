package org.openurp.platform.app

import org.beangle.data.model.bean.NamedBean
import org.beangle.data.model.IdGrowSlow
import org.beangle.data.model.bean.IntIdBean
import collection.mutable

class App extends IntIdBean with NamedBean with IdGrowSlow {
  var datasources: Seq[DataSource] = new mutable.ListBuffer[DataSource]
  var remark: String = _
}

class DataSourceCfg extends IntIdBean with NamedBean with IdGrowSlow {
  var url: String = _
  var driverClassName: String = _
  var remark: String = _
}

class DataSource extends IntIdBean with IdGrowSlow {
  var app: App = _
  var config: DataSourceCfg = _
  var username: String = _
  var password: String = _
  /**
   * 最大活动连接数
   */
  var maxActive: Int = _
  var key: String = _
  var remark: String = _
}