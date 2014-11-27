package org.openurp.resource.model

import org.beangle.data.model.bean.{ IntIdBean, NamedBean }
import org.openurp.app.App

class DbBean extends IntIdBean with NamedBean {
  var url: String = _
  var driverClassName: String = _
  var remark: String = _
}
