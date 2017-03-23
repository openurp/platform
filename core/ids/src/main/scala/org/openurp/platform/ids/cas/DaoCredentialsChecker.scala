package org.openurp.platform.ids.cas

import org.beangle.security.codec.DefaultPasswordEncoder
import org.beangle.security.authc.CredentialsChecker
import javax.sql.DataSource
import org.beangle.data.jdbc.query.JdbcExecutor
import org.beangle.security.codec.DefaultPasswordEncoder

/**
 * @author chaostone
 */
class DaoCredentialsChecker(dataSource: DataSource) extends CredentialsChecker {
  private val executor = new JdbcExecutor(dataSource)
  override def check(principal: Any, credential: Any): Boolean = {
    //FIXME school
    val rs = executor.query("select name from usr.users where code = ?", principal)
    if (rs.isEmpty) {
      false
    } else {
      val digest = rs.head.head.asInstanceOf[String]
      DefaultPasswordEncoder.verify(digest, credential.toString)
    }
  }
}