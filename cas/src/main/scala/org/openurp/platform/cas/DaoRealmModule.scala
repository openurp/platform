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
package org.openurp.platform.cas

import org.beangle.cdi.bind.BindModule
import org.beangle.data.jdbc.ds.DataSourceFactory
import org.beangle.security.authc.{ DefaultAccountRealm, RealmAuthenticator }
import org.openurp.app.UrpApp
import org.openurp.platform.cas.service.DaoAccountStore

class DaoRealmModule extends BindModule {
  override def binding() {
    bind("security.Realm.default", classOf[DefaultAccountRealm])
      .constructor(bean(classOf[DaoAccountStore]), ref("security.CredentialsChecker.default"))
    bind("security.Authenticator", classOf[RealmAuthenticator])
      .constructor(List(ref("security.Realm.default")))
  }
}
