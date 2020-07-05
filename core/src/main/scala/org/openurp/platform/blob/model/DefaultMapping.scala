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
package org.openurp.platform.blob.model

import org.beangle.data.orm.{IdGenerator, MappingModule}

object DefaultMapping extends MappingModule {

  def binding(): Unit = {
    defaultIdGenerator(classOf[Long],IdGenerator.DateTime)
    defaultCache("openurp.platform.security", "read-write")

    bind[Profile].declare { e =>
      e.path & e.name are length(100)
      e.users is length(200)
    }.generator(IdGenerator.AutoIncrement)

    bind[BlobMeta].declare { e =>
      e.owner is length(100)
      e.name is length(300)
      e.mediaType is length(60)
      e.path is length(400)
    }

    all.except(classOf[BlobMeta]).cacheAll()
  }

}
