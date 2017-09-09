/*
 * Beangle, Agile Development Scaffold and Toolkit
 *
 * Copyright (c) 2005-2017, Beangle Software.
 *
 * Beangle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beangle is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Beangle.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.platform.cas.service

import org.beangle.data.jdbc.query.JdbcExecutor
import org.beangle.security.authc.CredentialsChecker
import org.beangle.security.codec.DefaultPasswordEncoder
import org.beangle.security.realm.ldap.LdapUserStore

import javax.sql.DataSource

class DBLdapCredentialsChecker(dataSource: DataSource) extends CredentialsChecker {
  private val executor = new JdbcExecutor(dataSource)

  var passwordSql: String = "select password from usr.users where code = ?"

  var ldapUserStore: LdapUserStore = _
  override def check(principal: Any, credential: Any): Boolean = {
    val passwords = executor.query(passwordSql, principal)
    if (passwords.isEmpty) {
      false
    } else {
      val dbpwd = passwords.head.head.asInstanceOf[String]
      if (null == ldapUserStore) {
        DefaultPasswordEncoder.verify(dbpwd, credential.toString)
      } else {
        val uid = principal.toString
        ldapUserStore.getUserDN(uid) match {
          case Some(dn) =>
            ldapUserStore.getPassword(dn) match {
              case Some(p) =>
                val ldapCorrect = DefaultPasswordEncoder.verify(p, credential.toString)
                if (ldapCorrect && p != dbpwd) {
                  executor.update("update usr.users set password=? where code=? ", p, uid)
                }
                ldapCorrect
              case None => false
            }
          case None => DefaultPasswordEncoder.verify(dbpwd, credential.toString)
        }
      }
    }
  }
}
