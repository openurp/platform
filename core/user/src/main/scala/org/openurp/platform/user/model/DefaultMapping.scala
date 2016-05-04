package org.openurp.platform.user.model

import scala.reflect.runtime.universe
import org.beangle.data.model.bind.Mapping

object DefaultMapping extends Mapping {

  def binding(): Unit = {
    defaultIdGenerator("auto_increment")
    defaultCache("openurp.platform.security", "read-write")

    bind[Dimension].on(e => declare(
      e.name & e.title are (notnull, length(40)),
      e.source is length(1000),
      e.typeName is notnull,
      e.keyName is length(20),
      e.properties is length(100)))

    bind[RoleMember].on(e => declare(
      e.role & e.user & e.updatedAt are notnull))

    bind[Role].on(e => declare(
      e.indexno is notnull,
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
      e.roles is depends("user"),
      e.groups is depends("user"),
      e.properties is eleLength(2000)))

    bind[UserCategory].on(e => declare(
      e.code is (notnull, length(30), unique),
      e.name is (notnull, length(100))))

    bind[UserProfile].on(e => declare(
      e.user & e.domain are notnull,
      e.properties is eleLength(2000)))

    bind[GroupMember].on(e => declare(
      e.group & e.user & e.updatedAt are notnull))

    bind[Group].on(e => declare(
      e.indexno is notnull,
      e.name is (notnull, length(100)),
      e.children is depends("parent"),
      e.members is depends("group"),
      e.properties is eleLength(2000)))

    bind[Root].on(e => declare(
      e.app & e.user & e.updatedAt are notnull))

  }

}
