package org.openurp.platform.api.security

import org.beangle.security.authc.{ Account, AccountStore, DefaultAccount }
import org.openurp.platform.api.Urp
import org.openurp.platform.api.util.JSON
import org.beangle.commons.web.util.HttpUtils

import org.beangle.security.authc.{ Account, AccountStore, DefaultAccount }
import org.openurp.platform.api.Urp
import org.openurp.platform.api.util.JSON
import org.beangle.commons.web.util.HttpUtils

/**
 * @author chaostone
 * FIXME missing school
 */
class RemoteAccountStore extends AccountStore {

  def load(principal: Any): Option[Account] = {
    val url = Urp.platformBase + "/user/accounts/" + principal.toString + ".json"
    HttpUtils.getResponseText(url) match {
      case Some(u) =>
        val data = JSON.parse(u).asInstanceOf[collection.Map[String, _]]
        if (data.isEmpty) None
        else {
          val account = new DefaultAccount(principal.toString, data("description").toString)
          account.authorities = data("authorities").toString
          data.get("details") foreach { details =>
            account.details ++= details.asInstanceOf[collection.Map[String, String]]
          }
          account.accountLocked = data("accountLocked").asInstanceOf[Boolean]
          account.accountExpired = data("accountExpired").asInstanceOf[Boolean]
          account.credentialExpired = data("credentialExpired").asInstanceOf[Boolean]
          account.disabled = !data("enabled").asInstanceOf[Boolean]
          Some(account)
        }
      case None => None
    }
  }

}
