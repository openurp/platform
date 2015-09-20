package org.openurp.platform.kernel.model

import scala.reflect.runtime.universe

import org.beangle.data.model.bind.Mapping

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
      e.dataPermissions is depends("app"),
      e.datasources is depends("app")))

    bind[DataSource].on(e => declare(
      e.app & e.db & e.username & e.password & e.maxActive & e.name are notnull,
      e.username is length(100),
      e.password is length(200),
      e.name is length(100),
      e.remark is length(200)))

    bind[AppDataPermission].on(e => declare(
      e.app & e.resource are notnull,
      e.actions is length(500),
      e.restrictions is length(500)))

    bind[DataResource].on(e => declare(
      e.name & e.typeName are (notnull, length(200)),
      e.scope is notnull,
      e.title is (notnull, length(200)),
      e.remark & e.actions are length(200)))

    bind[Db].on(e => declare(
      e.name is (notnull, length(100), unique),
      e.driver is (notnull, length(100)),
      e.databaseName & e.serverName is length(100),
      e.url is (notnull, length(200)),
      e.remark is (length(200))))
  }

}