/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2005, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.platform.cas

import java.io.FileInputStream

import org.beangle.cdi.bind.BindModule
import org.beangle.ids.cas.service.DBLdapCredentialsChecker
import org.beangle.security.realm.ldap.{ PoolingContextSource, SimpleLdapUserStore }
import org.openurp.app.UrpApp

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
      .property("passwordSql", "select password from usr.users where code = ?")
  }
}
