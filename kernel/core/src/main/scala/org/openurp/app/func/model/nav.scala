package org.openurp.app.func.model

import collection.mutable
import org.beangle.data.model.bean.{ EnabledBean, HierarchicalBean, IntIdBean, NamedBean }
import org.beangle.security.blueprint.{ FuncResource => BeangleFuncResource }
import org.openurp.app.App
import org.openurp.app.func.FuncResource
import org.beangle.security.blueprint.{FuncResource => BeangleFuncResource}
import org.openurp.app.func.Menu
import org.openurp.app.func.MenuProfile

class MenuProfileBean extends IntIdBean with NamedBean with EnabledBean with MenuProfile {
  var app: App = _
  var menus: mutable.Buffer[Menu] = new mutable.ListBuffer[Menu]
}

class MenuBean extends IntIdBean with NamedBean with EnabledBean with HierarchicalBean[org.beangle.security.blueprint.Menu] with Menu {
  var profile: MenuProfile = _
  var title: String = _
  var entry: FuncResource = _
  var params: String = _
  var remark: String = _
  var resources: mutable.Set[BeangleFuncResource] = new mutable.HashSet[BeangleFuncResource]
}