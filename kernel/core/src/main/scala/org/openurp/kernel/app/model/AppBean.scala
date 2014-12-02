package org.openurp.kernel.app.model

import collection.mutable

import org.beangle.data.model.bean.{ IntIdBean, NamedBean }
import org.openurp.kernel.app.App
import org.openurp.kernel.app.func.model.{ AppFuncPermissionBean, FuncResourceBean }

class AppBean extends IntIdBean with NamedBean with App {
  var secret: String = _
  var title: String = _
  var datasources: Seq[DataSourceBean] = new mutable.ListBuffer[DataSourceBean]
  var funcResources: mutable.Set[FuncResourceBean] = new mutable.HashSet[FuncResourceBean]
  var funcPermissions: mutable.Seq[AppFuncPermissionBean] = new mutable.ListBuffer[AppFuncPermissionBean]
  var remark: String = _
  var appType: String = _
  var url: String = _
}