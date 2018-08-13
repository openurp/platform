/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2005, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.platform.user.model

import scala.reflect.runtime.universe
import org.beangle.data.orm.MappingModule
import org.beangle.data.orm.IdGenerator

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

    bind[Avatar].on(e => declare(
      e.id is length(50),
      e.fileName is length(50))).generator(IdGenerator.Assigned)

    bind[Root]

    all.cacheable()
  }

}
