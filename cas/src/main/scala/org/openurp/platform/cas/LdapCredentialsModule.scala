package org.openurp.platform.cas

import org.beangle.cdi.bind.BindModule
import org.beangle.security.realm.ldap.{ DefaultCredentialsChecker, PoolingContextSource, SimpleLdapUserStore }

class LdapCredentialsModule extends BindModule {
  override def binding() {
    bind("security.ldap.source", classOf[PoolingContextSource])
      .constructor($("ldap.url"), $("ldap.user"), $("ldap.password"))
    bind("security.LdapUserStore.default", classOf[SimpleLdapUserStore])
      .constructor(ref("security.ldap.source"), $("ldap.base"))
    bind("security.CredentialsChecker.default", classOf[DefaultCredentialsChecker])
  }
}
