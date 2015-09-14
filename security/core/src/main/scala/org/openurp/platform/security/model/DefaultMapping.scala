package org.openurp.platform.security.model

import scala.reflect.runtime.universe
import org.beangle.data.model.bind.Mapping

object DefaultMapping extends Mapping {

  def binding(): Unit = {
    defaultIdGenerator("auto_increment")
    defaultCache("openurp.platform.security", "read-write")

    bind[Dimension].on(e => declare(
      e.name & e.title are (notnull, length(40)),
      e.source is length(500),
      e.typeName is notnull,
      e.keyName is length(20),
      e.properties is length(100)))

    bind[FuncPermission].on(e => declare(
      e.role & e.resource & e.beginAt are notnull,
      e.actions is length(100),
      e.restrictions is length(100),
      e.remark is length(100)))

    bind[Member].on(e => declare(
      e.role & e.user & e.updatedAt are notnull)).table("members")

    bind[Role].on(e => declare(
      e.app & e.indexno are notnull,
      e.name is (notnull, length(100)),
      e.children is depends("parent"),
      e.members is depends("role"),
      e.properties is eleLength(2000)))

    bind[User].on(e => declare(
      e.code is (notnull, length(30), unique),
      e.name is (notnull, length(100)),
      e.category is notnull,
      e.password is (notnull, length(200)),
      e.remark is length(100),
      e.members is depends("user"),
      e.profiles is depends("user"),
      e.properties is eleLength(2000)))

    bind[UserCategory].on(e => declare(
      e.code is (notnull, length(30), unique),
      e.name is (notnull, length(100))))

    bind[UserProfile].on(e => declare(
      e.user & e.app are notnull,
      e.properties is eleLength(2000)))

    bind[Menu].on(e => declare(
      e.profile & e.indexno & e.name & e.title are notnull,
      e.name & e.title & e.remark are length(100),
      e.indexno is length(50),
      e.children is depends("parent"),
      e.params is length(200)))

    bind[MenuProfile].on(e => declare(
      e.app is notnull,
      e.name is (notnull, length(100)),
      e.menus is depends("profile")))

    bind[FuncResource].on(e => declare(
      e.name is (notnull, length(200)),
      e.app & e.scope are notnull,
      e.title is (notnull, length(200)),
      e.remark & e.actions are length(200)))

    bind[DataPermission].on(e => declare(
      e.resource & e.beginAt are notnull,
      e.filters is (notnull, length(600))))

    bind[SessionProfileBean].on(e => declare(
      e.app & e.category are notnull)).cacheable()

    bind[Root].on(e => declare(
      e.app & e.user & e.updatedAt are notnull))

  }

}