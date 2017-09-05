package net.openurp.sfu.platform.cas.service

import org.beangle.security.authc.CredentialsChecker
import javax.sql.DataSource
import org.beangle.data.jdbc.query.JdbcExecutor
import org.beangle.security.codec.DefaultPasswordEncoder
import org.beangle.security.realm.ldap.LdapUserStore

class DbLdapCredentialsChecker(dataSource: DataSource, passwordSql: String) extends CredentialsChecker {
  private val executor = new JdbcExecutor(dataSource)

  //"select password from usr.users where code = ?"
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
