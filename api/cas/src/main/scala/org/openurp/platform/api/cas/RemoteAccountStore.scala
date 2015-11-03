package org.openurp.platform.api.cas

import org.beangle.security.authc.{ Account, AccountStore, DefaultAccount }
import org.openurp.platform.api.app.UrpApp
import org.openurp.platform.api.Urp
import org.openurp.platform.api.util.JSON
import org.beangle.commons.web.util.HttpUtils
import java.net.URL
import org.beangle.commons.collection.Properties

/**
 * @author chaostone
 */
class RemoteAccountStore extends AccountStore {

  def load(principal: Any): Option[Account] = {
    val url = Urp.platformBase + "/security/" + UrpApp.name + "/accounts/" + principal.toString + ".json"
    val data = JSON.parse(HttpUtils.getResponseText(url)).asInstanceOf[collection.Map[String, _]]
    val account = new DefaultAccount(principal, data("description").toString)
    account.authorities = data("authorities").asInstanceOf[collection.Iterable[Number]].map { x => x.intValue }.toSet
    account.details ++= data("details").asInstanceOf[collection.Map[String, _]]
    account.accountLocked = data("accountLocked").asInstanceOf[Boolean]
    account.accountExpired = data("accountExpired").asInstanceOf[Boolean]
    account.credentialExpired = data("credentialExpired").asInstanceOf[Boolean]
    account.disabled = data("disabled").asInstanceOf[Boolean]
    Some(account)
  }

}