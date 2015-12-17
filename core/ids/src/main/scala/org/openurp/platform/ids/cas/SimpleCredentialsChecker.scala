package org.openurp.platform.ids.cas

import org.beangle.security.codec.DefaultPasswordEncoder
import org.beangle.security.authc.CredentialsChecker
import javax.sql.DataSource
import org.beangle.data.jdbc.query.JdbcExecutor

/**
 * @author chaostone
 */
class SimpleCredentialsChecker(dataSource: DataSource) extends CredentialsChecker {
  private val executor = new JdbcExecutor(dataSource)
  override def check(principal: Any, credential: Any): Boolean = {
    !executor.query("select name from se.users where code = ? and password=?", principal, credential).isEmpty
  }
}