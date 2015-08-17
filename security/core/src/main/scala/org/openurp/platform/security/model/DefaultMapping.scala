package org.openurp.platform.security.model

import scala.reflect.runtime.universe
import org.beangle.commons.lang.annotation.beta
import org.beangle.data.model.bind.Mapping
import org.beangle.security.blueprint.{ FuncResource, Menu, MenuProfile }
import org.beangle.security.blueprint.Field
import org.beangle.security.blueprint.FuncPermission
import org.beangle.security.blueprint.Role
import org.beangle.security.blueprint.User
import org.beangle.security.blueprint.Profile

object DefaultMapping extends Mapping {

  def binding(): Unit = {
    defaultIdGenerator("auto_increment")

    bind[UrpField](classOf[Field].getName).on(e => declare(
      e.name & e.title are (notnull, length(40)),
      e.source is length(500),
      e.typeName is notnull,
      e.keyName is length(20),
      e.properties is length(100)))

    bind[UrpFuncPermission](classOf[FuncPermission].getName).on(e => declare(
      e.role & e.resource & e.beginAt are notnull,
      e.actions is length(100),
      e.restrictions is length(100),
      e.remark is length(100)))

    bind[UrpMember].on(e => declare(
      e.role & e.user & e.updatedAt are notnull)).table("members")

    bind[UrpRole](classOf[Role].getName).on(e => declare(
      e.app & e.indexno are notnull,
      e.name is (notnull, length(100)),
      e.children is depends("parent"),
      e.members is depends("role"),
      e.properties is eleLength(2000)))

    bind[UrpUser](classOf[User].getName).on(e => declare(
      e.code is (notnull, length(30), unique),
      e.name is (notnull, length(100)),
      e.category is notnull,
      e.password is (notnull, length(200)),
      e.remark is length(100),
      e.members is depends("user"),
      e.profiles is depends("user")))

    bind[UserCategory].on(e => declare(
      e.code is (notnull, length(30), unique),
      e.name is (notnull, length(100))))

    bind[UrpUserProfile](classOf[Profile].getName).on(e => declare(
      e.user is notnull,
      e.properties is eleLength(2000)))
  }

}