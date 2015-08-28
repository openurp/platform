package org.openurp.platform.security.model

import scala.reflect.runtime.universe
import org.beangle.data.model.bind.Mapping
import org.beangle.security.blueprint.{ Dimension, FuncPermission, Menu, MenuProfile, Profile, Role, User }
import org.beangle.security.blueprint.DataResource
import org.beangle.security.blueprint.DataPermission
import org.beangle.security.session.SessionProfile

object DefaultMapping extends Mapping {

  def binding(): Unit = {
    defaultIdGenerator("auto_increment")

    bind[UrpDimension](classOf[Dimension].getName).on(e => declare(
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
      e.profiles is depends("user"),
      e.properties is eleLength(2000)))

    bind[UserCategory].on(e => declare(
      e.code is (notnull, length(30), unique),
      e.name is (notnull, length(100))))

    bind[UrpUserProfile](classOf[Profile].getName).on(e => declare(
      e.user & e.app are notnull,
      e.properties is eleLength(2000)))

    bind[AppMenu](classOf[Menu].getName).on(e => declare(
      e.profile & e.indexno & e.name & e.title are notnull,
      e.name & e.title & e.remark are length(100),
      e.indexno is length(50),
      e.children is depends("parent"),
      e.params is length(200)))

    bind[AppMenuProfile](classOf[MenuProfile].getName).on(e => declare(
      e.app is notnull,
      e.name is (notnull, length(100)),
      e.menus is depends("profile")))

    bind[UrpDataResource](classOf[DataResource].getName).on(e => declare(
      e.name is (notnull, length(200)),
      e.title is (notnull, length(100))))

    bind[UrpDataPermission](classOf[DataPermission].getName).on(e => declare(
      e.resource & e.beginAt are notnull,
      e.filters is (notnull, length(600))))

    bind[UrpSessionProfile](classOf[SessionProfile].getName).on(e => declare(
      e.app & e.role are notnull))

  }

}