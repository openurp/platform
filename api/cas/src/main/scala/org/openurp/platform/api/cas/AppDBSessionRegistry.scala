package org.openurp.platform.api.cas
import org.beangle.security.session.jdbc.DBSessionRegistry
import org.beangle.security.session.jdbc.DBSessionRegistry
import org.beangle.data.jdbc.query.JdbcExecutor
import org.beangle.security.session.SessionBuilder
import org.beangle.data.dao.EntityDao
import org.openurp.platform.api.app.UrpApp
import org.beangle.commons.lang.Strings
import javax.sql.DataSource
/**
 * @author chaostone
 */
class AppDBSessionRegistry(dataSource: DataSource) extends DBSessionRegistry(dataSource) {

  var entityDao: EntityDao = _

  override def init() {
    var appId = UrpApp.name
    appId = Strings.replace(appId, "-", "_")
    appId = Strings.substringAfter(appId, "_")
    this.sessionTable = "app_" + appId + "_session_infoes"
    this.statTable = "app_" + appId + "_session_stats"
    super.init()
  }

}