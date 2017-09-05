package org.openurp.platform.cas

import org.beangle.cdi.bind.BindModule
import org.beangle.data.jdbc.ds.DataSourceFactory
import org.beangle.security.authc.{ DefaultAccountRealm, RealmAuthenticator }
import org.openurp.app.UrpApp
import org.openurp.platform.cas.service.DaoAccountStore

class DaoRealmModule extends BindModule {
  override def binding() {
    bind("DataSource.security", classOf[DataSourceFactory]).property("name", "security")
      .property("url", UrpApp.getUrpAppFile.get.getAbsolutePath).primary()

    bind("security.Realm.default", classOf[DefaultAccountRealm])
      .constructor(bean(classOf[DaoAccountStore]), ref("security.CredentialsChecker.default"))
    bind("security.Authenticator", classOf[RealmAuthenticator])
      .constructor(List(ref("security.Realm.default")))
  }
}
