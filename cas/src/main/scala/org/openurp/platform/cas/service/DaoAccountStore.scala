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
package org.openurp.platform.cas.service

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.data.hibernate.spring.SessionUtils
import org.beangle.security.authc.{Account, AccountStore, DefaultAccount}
import org.hibernate.SessionFactory
import org.openurp.platform.user.model.{RoleMember, UserProfile}
import org.openurp.platform.user.service.UserService

class DaoAccountStore(userService: UserService, entityDao: EntityDao, sf: SessionFactory) extends AccountStore {

  def load(principal: Any): Option[Account] = {
    SessionUtils.enableBinding(sf)
    try {
      val rs = userService.get(principal.toString) match {
        case Some(user) =>
          val account = new DefaultAccount(user.code, user.name)
          account.accountExpired = user.accountExpired
          account.accountLocked = user.locked
          account.credentialExpired = user.credentialExpired
          account.disabled = !user.enabled
          account.categoryId = user.category.id

          val query = OqlBuilder.from[Int](classOf[RoleMember].getName, "rm")
            .where("rm.user=:user and rm.member=true", user)
            .select("rm.role.id")
          val rs = entityDao.search(query)
          account.authorities = rs.map(_.toString).toArray

          val upQuery = OqlBuilder.from(classOf[UserProfile], "up").where("up.user=:user", user)
          val ups = entityDao.search(upQuery)

          if (ups.nonEmpty) {
            val domainUps = ups.groupBy(up => up.domain)
            domainUps foreach {
              case (domain, dups) =>
                val str = new StringBuilder
                str += '['
                val profiles = Collections.newBuffer[String]
                dups foreach { up =>
                  val ps = up.properties.map (e => s""""${e._1.name}":"${e._2}"""")
                  profiles += "{" + ps.mkString(",") + "}"
                }
                str ++= profiles.mkString(",")
                str += ']'
                account.details += "profiles_" + domain.name -> str.toString
            }
          }

          Some(account)
        case None => None
      }
      rs
    } finally {
      SessionUtils.disableBinding(sf)
      SessionUtils.closeSession(sf)
    }
  }
}
