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

import org.beangle.data.model.StringId
import java.time.LocalDate
import java.time.LocalDateTime
import org.beangle.data.model.pojo.Updated

object Avatar {
  var MaxSize = 500 * 1024 //500k
}

class Avatar extends StringId with Updated {

  var user: User = _

  var image: Array[Byte] = _

  var path:String = _

  var fileName: String = _

  def this(user: User) {
    this()
    this.user = user
  }
}
