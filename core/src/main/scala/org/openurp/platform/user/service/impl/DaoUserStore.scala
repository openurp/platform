
package org.openurp.platform.user.service.impl

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.security.authc.{ Account, AccountStore, DefaultAccount }
import org.openurp.platform.user.model.{ RoleMember, UserProfile }
import org.openurp.platform.user.service.UserService

class DaoUserStore(userService: UserService, entityDao: EntityDao) extends AccountStore {

  def load(principal: Any): Option[Account] = {

    userService.get(principal.toString) match {
      case Some(user) =>
        val account = new DefaultAccount(user.code, user.name)
        account.accountExpired = user.accountExpired
        account.accountLocked = user.locked
        account.credentialExpired = user.credentialExpired
        account.disabled = !user.enabled
        account.details += "category" -> user.category.id.toString

        val query = OqlBuilder.from[Int](classOf[RoleMember].getName, "rm")
          .where("rm.user=:user and rm.member=true", user)
          .select("rm.role.id")
        val rs = entityDao.search(query)
        account.authorities = rs.mkString(",")

        val ups = entityDao.search(OqlBuilder.from(classOf[UserProfile], "up").where("up.user=:user", user))
        if (!ups.isEmpty) {
          val profiles = Collections.newBuffer[String]
          ups foreach { up =>
            val ps = up.properties map (e => e._1.name + ":" + e._2)
            profiles += ps.mkString(",")
          }
          account.details += "profiles" -> ("[" + profiles.mkString(",") + "]")
        }

        Some(account)
      case None => None
    }
  }
}
