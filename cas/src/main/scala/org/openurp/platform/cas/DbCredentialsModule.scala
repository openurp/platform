package org.openurp.platform.cas

import org.beangle.cdi.bind.BindModule
import org.beangle.ids.cas.service.DaoCredentialsChecker
import org.openurp.platform.cas.service.DBLdapCredentialsChecker

class DbCredentialsModule extends BindModule {
  override def binding() {
    bind("security.CredentialsChecker.default", classOf[DBLdapCredentialsChecker])
      .constructor(ref("DataSource.security"), "select password from usr.users where code=?")
  }
}

