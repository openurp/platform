package org.openurp.platform.security.model

import scala.reflect.runtime.universe
import org.beangle.data.orm.MappingModule

object DefaultMapping extends MappingModule {

  def binding(): Unit = {
    defaultIdGenerator("auto_increment")
    defaultCache("openurp.platform.security", "read-write")

    bind[FuncPermission].on(e => declare(
      e.role & e.resource & e.beginAt are notnull,
      e.actions is length(100),
      e.restrictions is length(100),
      e.remark is length(100)))

    bind[Menu].on(e => declare(
      e.app & e.indexno & e.name & e.title are notnull,
      e.name & e.title & e.remark are length(100),
      e.indexno is length(50),
      e.children is (depends("parent"), orderby("indexno")),
      e.params is length(200)))

    bind[FuncResource].on(e => declare(
      e.name is (notnull, length(200)),
      e.app & e.scope are notnull,
      e.title is (notnull, length(200)),
      e.remark & e.actions are length(200)))

    bind[DataResource].on(e => declare(
      e.name & e.typeName are  length(200),
      e.title is   length(200),
      e.remark & e.actions are length(200)))

    bind[DataPermission].on(e => declare(
      e.description is length(100),
      e.filters is length(600)))

    bind[AppPermission].on(e => declare(
      e.app & e.resource are notnull,
      e.actions is length(500),
      e.restrictions is length(500)))

  }

}
