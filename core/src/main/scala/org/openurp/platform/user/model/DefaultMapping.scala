/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.platform.user.model

import org.beangle.data.orm.{IdGenerator, MappingModule}

object DefaultMapping extends MappingModule {

  def binding(): Unit = {
    defaultIdGenerator("auto_increment")
    defaultCache("openurp.platform.security", "read-write")

    bind[Dimension].declare { e =>
      e.name & e.title are length(40)
      e.source is length(6000)
      e.typeName is notnull
      e.keyName is length(20)
      e.properties is length(100)
    }

    bind[RoleMember].declare { e =>
      index("idx_role_member_user", false, e.user)
    }

    bind[Role].declare { e =>
      e.getName is length(100)
      e.children is depends("parent")
      e.members is depends("role")
      e.properties is eleLength(2000)
    }

    bind[User].declare { e =>
      e.code is(length(30), unique)
      e.getName is length(100)
      e.password is length(200)
      e.remark is length(100)
      e.roles is depends("user")
      e.groups is depends("user")
      e.properties is eleLength(2000)
    }

    bind[UserCategory].declare { e =>
      e.code is(length(30), unique)
      e.name is length(100)
    }

    bind[UserProfile].declare { e =>
      e.properties is eleLength(2000)
    }

    bind[GroupMember]

    bind[Group].declare { e =>
      e.getName is length(100)
      e.children is depends("parent")
      e.members is depends("group")
      e.properties is eleLength(2000)
    }

    bind[Avatar].declare { e =>
      e.id is length(50)
      e.image is length(Avatar.MaxSize)
      e.fileName is length(50)
    }.generator(IdGenerator.Assigned)

    bind[Root]
    bind[Message]
    bind[Notification]
    bind[Todo]

    all.except(classOf[Avatar], classOf[RoleMember], classOf[GroupMember]).cacheAll()
  }

}
