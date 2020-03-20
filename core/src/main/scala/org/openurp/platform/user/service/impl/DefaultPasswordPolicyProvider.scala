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
package org.openurp.platform.user.service.impl

import org.beangle.commons.bean.Initializing
import org.beangle.data.dao.EntityDao
import org.beangle.security.authc.{PasswordPolicy, PasswordPolicyProvider}
import org.openurp.platform.user.model.PasswordConfig

class DefaultPasswordPolicyProvider extends PasswordPolicyProvider with Initializing {
  var entityDao: EntityDao = _
  var policy: PasswordPolicy = _

  override def init(): Unit = {
    val configs = entityDao.getAll(classOf[PasswordConfig])
    policy =
      if (configs.size > 0) {
        configs.head
      } else {
        PasswordPolicy.Medium
      }
  }

  override def getPolicy: PasswordPolicy = policy
}
