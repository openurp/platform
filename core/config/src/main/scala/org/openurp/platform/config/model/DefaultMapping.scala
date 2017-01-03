package org.openurp.platform.config.model

import scala.reflect.runtime.universe

import org.beangle.commons.model.bind.Mapping

object DefaultMapping extends Mapping {

  def binding(): Unit = {
    defaultIdGenerator("auto_increment")
    bind[App].on(e => declare(
      e.name is (length(100), unique),
      e.title is length(100),
      e.secret is length(200),
      e.url is length(200),
      e.appType is length(50),
      e.remark is length(200),
      e.indexno is length(50),
      e.datasources is depends("app")))

    bind[DataSource].on(e => declare(
      e.username is length(100),
      e.password is length(200),
      e.name is length(100),
      e.remark is length(200)))

    bind[Db].on(e => declare(
      e.name is (length(100), unique),
      e.driver is (length(100)),
      e.databaseName & e.serverName is length(100),
      e.url is length(200),
      e.remark is (length(200))))

    bind[Domain].on(e => declare(
      e.name is (length(100), unique),
      e.title is length(200),
      e.children is depends("parent")))
  }

}
