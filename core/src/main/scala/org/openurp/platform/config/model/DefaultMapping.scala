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
package org.openurp.platform.config.model

import org.beangle.data.orm.{IdGenerator, MappingModule}

object DefaultMapping extends MappingModule {

  def binding(): Unit = {
    defaultIdGenerator(classOf[Int], IdGenerator.AutoIncrement)
    defaultIdGenerator(classOf[Long], IdGenerator.AutoIncrement)
    defaultCache("openurp.platform.security", "read-write")

    bind[Org].declare { e =>
      e.code is(length(50), unique)
      e.name & e.shortName are length(100)
      e.wwwUrl & e.logoUrl are length(200)
    }

    bind[App].declare { e =>
      e.getName is length(100)
      e.title is length(100)
      e.secret is length(200)
      e.url is length(200)
      e.navStyle is length(50)
      e.remark is length(200)
      e.indexno is length(50)
      e.datasources is depends("app")
    }

    bind[AppGroup].declare { e =>
       e.name is length(100)
    }

    bind[Credential].declare { e =>
      e.username is length(100)
      e.password is length(200)
      e.name is length(100)
    }

    bind[DataSource].declare { e =>
      e.name is length(100)
      e.remark is length(200)
    }

    bind[Db].declare { e =>
      e.name is(length(100), unique)
      e.driver is length(100)
      e.databaseName & e.serverName is length(100)
      e.url is length(200)
      e.remark is length(200)
    }

    bind[Domain].declare { e =>
      e.name is(length(100), unique)
      e.hostname is (length(100),unique)
      e.title is length(200)
    }

    bind[AppType]

    all.cacheAll()
  }

}
