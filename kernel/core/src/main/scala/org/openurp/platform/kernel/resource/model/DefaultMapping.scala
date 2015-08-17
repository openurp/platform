package org.openurp.platform.kernel.resource.model

import scala.reflect.runtime.universe
import org.beangle.commons.lang.annotation.beta
import org.beangle.data.model.bind.Mapping
import org.beangle.security.blueprint.{ FuncResource, Menu, MenuProfile }
import org.openurp.platform.kernel.app.model.{ AppFuncPermission, AppFuncResource, AppMenu }

object DefaultMapping extends Mapping {

  def binding(): Unit = {
    defaultIdGenerator("auto_increment")
    bind[Db].on(e => declare(
      e.name is (notnull, length(100), unique),
      e.driverClassName is (notnull, length(100)),
      e.url is (notnull, length(200)),
      e.remark is (length(200))))
  }

}