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

import java.time.LocalDate

import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.Updated

class Credential extends LongId with Updated {

  var user: User = _

  var password: String = _

  var expiredOn: LocalDate = _

  var inactiveOn: LocalDate = _

  def expired: Boolean = {
    LocalDate.now.isAfter(expiredOn)
  }

  def inactive: Boolean = {
    LocalDate.now.isAfter(inactiveOn)
  }
}
