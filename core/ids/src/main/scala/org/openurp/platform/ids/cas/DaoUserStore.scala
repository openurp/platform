package org.openurp.platform.ids.cas

import org.beangle.data.jdbc.query.JdbcExecutor
import org.beangle.security.authc.{ Account, AccountStore, DefaultAccount }

import javax.sql.DataSource

/**
 * @author chaostone
 */
class DaoUserStore(dataSource: DataSource) extends AccountStore {
  private val executor = new JdbcExecutor(dataSource)
  //FIXME load status from db
  override def load(principal: Any): Option[Account] = {
    val users = executor.query("select name from usr.users where code = ?", principal)
    if (users.isEmpty) None
    else Some(new DefaultAccount(principal, users.head.head.asInstanceOf[String]))
  }
}
