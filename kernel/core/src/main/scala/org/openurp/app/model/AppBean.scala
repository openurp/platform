package org.openurp.app.model

import collection.mutable
import org.beangle.data.model.bean.{ IntIdBean, NamedBean }
import org.openurp.app.App
import org.openurp.app.resource.model.DataSourceBean
import org.openurp.app.func.model.FuncResourceBean
import org.openurp.app.func.model.AppFuncPermissionBean

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