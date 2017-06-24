
package org.openurp.platform.user.service.impl

import org.beangle.data.dao.EntityDao
import org.beangle.security.authc.{ Account, AccountStore, DefaultAccount }
import org.openurp.platform.user.service.UserService
import org.beangle.data.dao.OqlBuilder
import org.openurp.platform.user.model.RoleMember

class DaoUserStore(userService: UserService, entityDao: EntityDao) extends AccountStore {

  def load(principal: Any): Option[Account] = {

    userService.get(principal.toString) match {
      case Some(user) =>
        val account = new DefaultAccount(user.code, user.name)
        account.accountExpired = user.accountExpired
        account.accountLocked = user.locked
        account.credentialExpired = user.credentialExpired
        account.disabled = !user.enabled
        account.details += "category" -> user.category.id

        val query = OqlBuilder.from[Int](classOf[RoleMember].getName, "rm")
          .where("rm.user=:user and rm.member=true", user)
          .select("rm.role.id")
        val rs = entityDao.search(query)
        account.authorities = rs.toSet
        Some(account)
      case None => None
    }
  }
}
