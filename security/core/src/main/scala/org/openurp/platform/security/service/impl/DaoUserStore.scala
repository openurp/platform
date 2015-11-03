
package org.openurp.platform.security.service.impl

import org.beangle.data.dao.EntityDao
import org.beangle.security.authc.{ Account, AccountStore, DefaultAccount }
import org.openurp.platform.api.app.UrpApp
import org.openurp.platform.security.service.UserService
import org.beangle.data.dao.OqlBuilder
import org.openurp.platform.security.model.FuncPermission

class DaoUserStore(userService: UserService, entityDao: EntityDao) extends AccountStore {

  def load(principal: Any): Option[Account] = {

    userService.get(principal.toString) match {
      case Some(user) =>
        val account = new DefaultAccount(user.code, user.name)
        account.accountExpired = user.accountExpired
        account.accountLocked = user.locked
        account.credentialExpired = user.credentialExpired
        account.disabled = !user.enabled
        val query = OqlBuilder.from(classOf[FuncPermission], "fp").join("fp.role.members", "m").where("m.member=true and m.user=:user", user)
          .where("fp.resource.app.name=:appName", UrpApp.name).where("fp.endAt is null or fp.endAt < :now)", new java.util.Date).select("fp.id")
        account.authorities = entityDao.search(query).toSet
        //account.details += "credential" -> user.credential
        account.details += "isRoot" -> userService.isRoot(user, UrpApp.name)
        Some(account)
      case None => None
    }
  }
}
