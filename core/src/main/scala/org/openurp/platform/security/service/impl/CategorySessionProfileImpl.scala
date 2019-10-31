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
package org.openurp.platform.security.service.impl

import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.security.authc.Account
import org.beangle.security.session.{SessionProfile, SessionProfileProvider}
import org.openurp.platform.session.model.SessionConfig

class CategorySessionProfileImpl extends SessionProfileProvider {
  var entityDao: EntityDao = _

  override def getProfile(account: Account): SessionProfile = {
    getProfile(account.categoryId)
  }

  private def getDefaultProfile: SessionProfile = {
    SessionProfile(30, 1, Int.MaxValue, checkConcurrent = false, checkCapacity = false)
  }

  private def getProfile(categoryId: Int): SessionProfile = {
    val builder = OqlBuilder.from(classOf[SessionConfig], "sc").where("sc.category.id=:categoryId", categoryId)
      .cacheable(true)
    val rs = entityDao.search(builder)
    if (rs.isEmpty) {
      getDefaultProfile
    } else {
      val p = rs.head
      SessionProfile(p.ttiMinutes, p.concurrent, p.capacity, p.checkConcurrent, p.checkCapacity)
    }
  }
}
