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

import org.beangle.data.model.StringId
import java.time.LocalDate
import java.time.LocalDateTime

class Avatar extends StringId {

  var user: User = _

  var updatedAt: LocalDateTime = _

  var image: Array[Byte] = _

  var fileName: String = _

  def this(user: User, image: Array[Byte]) {
    this()
    this.user = user
    this.image = image
  }
}
