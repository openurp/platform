package org.openurp.platform.user.model

import scala.reflect.runtime.universe
import org.beangle.data.orm.MappingModule

object DefaultMapping extends MappingModule {

  def binding(): Unit = {
    defaultIdGenerator("auto_increment")
    defaultCache("openurp.platform.security", "read-write")

    bind[Dimension].on(e => declare(
      e.name & e.title are length(40),
      e.source is length(6000),
      e.typeName is notnull,
      e.keyName is length(20),
      e.properties is length(100)))

    bind[RoleMember]

    bind[Role].on(e => declare(
      e.getName is length(100),
      e.children is depends("parent"),
      e.members is depends("role"), //donot cache
      e.properties is (eleLength(2000), cacheable)))

    bind[User].on(e => declare(
      e.code is (length(30), unique),
      e.getName is length(100),
      e.password is length(200),
      e.remark is length(100),
      e.roles is (depends("user"), cacheable),
      e.groups is (depends("user"), cacheable),
      e.properties is (eleLength(2000), cacheable)))

    bind[UserCategory].on(e => declare(
      e.code is (length(30), unique),
      e.name is length(100)))

    bind[UserProfile].on(e => declare(
      e.properties is (eleLength(2000), cacheable)))

    bind[GroupMember]

    bind[Group].on(e => declare(
      e.getName is length(100),
      e.children is depends("parent"),
      e.members is depends("group"),
      e.properties is (eleLength(2000), cacheable)))

    bind[Root]

    all.cacheable()
  }

}
