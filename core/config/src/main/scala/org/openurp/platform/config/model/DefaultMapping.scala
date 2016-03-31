package org.openurp.platform.config.model

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
      e.indexno is (notnull, length(50)),
      e.datasources is depends("app")))

    bind[DataSource].on(e => declare(
      e.app & e.db & e.username & e.password & e.maxActive & e.name are notnull,
      e.username is length(100),
      e.password is length(200),
      e.name is length(100),
      e.remark is length(200)))

    bind[Db].on(e => declare(
      e.name is (notnull, length(100), unique),
      e.driver is (notnull, length(100)),
      e.databaseName & e.serverName is length(100),
      e.url is (length(200)),
      e.remark is (length(200))))
  }

}
