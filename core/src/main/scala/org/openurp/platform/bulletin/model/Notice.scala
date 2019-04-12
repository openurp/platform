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
package org.openurp.platform.bulletin.model

import java.time.LocalDate

import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.DateRange
import org.openurp.platform.config.model.App
import org.openurp.platform.user.model.UserCategory
import org.openurp.platform.user.model.User

class Notice extends LongId with DateRange {

  var app: App = _

  var title: String = _

  var content: String = _

  var userCategory: UserCategory = _

  var sticky: Boolean = _

  var publishedOn: LocalDate = _

  var operator: User = _

  var archived: Boolean = _

  var popup: Boolean = _
}
