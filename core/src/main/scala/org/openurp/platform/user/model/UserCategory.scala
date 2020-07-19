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

import org.beangle.data.model.IntId
import org.beangle.data.model.pojo.{Coded, Named, Remark, TemporalOn, Updated}
import org.openurp.platform.config.model.Org

/**
 * 用户分类
 * @author chaostone
 */
class UserCategory extends IntId with Coded with TemporalOn with Named with Updated with Remark {
  var enName: String = _
  var org: Org = _
  override def toString = {
    name
  }
}
