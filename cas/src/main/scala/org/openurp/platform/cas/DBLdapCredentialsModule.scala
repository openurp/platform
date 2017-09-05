package org.openurp.platform.cas

import java.io.FileInputStream

import org.beangle.cdi.bind.BindModule
import org.beangle.security.realm.ldap.{ PoolingContextSource, SimpleLdapUserStore }
import org.openurp.app.UrpApp
import org.openurp.platform.cas.service.DBLdapCredentialsChecker

class DBLdapCredentialsModule extends BindModule {
  override def binding() {
    UrpApp.getUrpAppFile foreach { file =>
      val is = new FileInputStream(file)
      val app = scala.xml.XML.load(is)
      if ((app \\ "ldap").size > 0) {
        bind("security.ldap.source", classOf[PoolingContextSource])
          .constructor($("ldap.url"), $("ldap.user"), $("ldap.password"))
        bind("security.LdapUserStore.default", classOf[SimpleLdapUserStore])
          .constructor(ref("security.ldap.source"), $("ldap.base"))
      }
      is.close()
    }
    bind("security.CredentialsChecker.default", classOf[DBLdapCredentialsChecker])
      .constructor(ref("DataSource.security"))
  }
}
