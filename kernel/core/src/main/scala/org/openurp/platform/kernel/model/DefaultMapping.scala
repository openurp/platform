package org.openurp.platform.kernel.model

import scala.reflect.runtime.universe

import org.beangle.commons.lang.annotation.beta
import org.beangle.data.model.bind.Mapping
import org.beangle.security.blueprint.{ FuncResource, Menu, MenuProfile }

object DefaultMapping extends Mapping {

  def binding(): Unit = {
    defaultIdGenerator("auto_increment")
    bind[App].on(e => declare(
      e.name is (notnull, length(100), unique),
      e.title is (notnull, length(100)),
      e.secret is (notnull, length(200)),
      e.url is (notnull, length(200)),
      e.appType is (notnull, length(50)),
      e.remark is (length(200)),
      e.funcResources is depends("app"),
      e.datasources is depends("app")))

    bind[DataSource].on(e => declare(
      e.app & e.db & e.username & e.password & e.maxActive & e.name are notnull,
      e.username is length(100),
      e.password is length(200),
      e.name is length(100),
      e.remark is length(200)))

    bind[AccessToken].on(e => declare(
      e.app is (unique, notnull),
      e.token is (unique, notnull, length(200)),
      e.expiredAt is notnull))

    bind[AppFuncPermission].on(e => declare(
      e.app & e.resource are notnull,
      e.actions is length(500),
      e.restrictions is length(500)))

    bind[AppFuncResource](classOf[FuncResource].getName).on(e => declare(
      e.name is (notnull, length(200)),
      e.app & e.scope are notnull,
      e.title is (notnull, length(200)),
      e.remark & e.actions are length(200)))
      
    bind[Db].on(e => declare(
      e.name is (notnull, length(100), unique),
      e.driverClassName is (notnull, length(100)),
      e.url is (notnull, length(200)),
      e.remark is (length(200))))
  }

}