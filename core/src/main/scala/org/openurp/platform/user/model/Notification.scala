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

import java.time.LocalDateTime
import org.beangle.data.model.LongId
import java.time.Instant

object Notification {
  val Information = 1
  val Warning = 2
}

class Notification extends LongId {
  /** 主题 */
  var subject: String = _

  /** 内容 */
  var content: String = _

  /**接受人*/
  var recipient: User = _

  /** 发送日期 */
  var sentAt: Instant = _

  var importance: Int = _
}